package scraping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import log.OriginalLogger;
import models.StutsCen;
import util.DBUtil;

public class Scraping {
    public static void GetDataFromURL () throws IOException{
        // 変数定義
        int systemYear = YearMonth.now().getYear(); // 今年
        Document allYearHtml = (Document) Jsoup.connect("http://www.baseball-reference.com/register/league.cgi?code=JPPL&class=Fgn").get(); // Document形式のスクレイピング対象のページ
        Elements linkAllyaerTable = allYearHtml.select("#lg_history tbody");  // 毎年度、全チームの情報が入ったテーブル

        // ロガー
        String logPath = "C:\\pleiades\\workspace\\BaseballStuts\\Log\\";
        OriginalLogger originalLogger = new OriginalLogger();
        Logger logger = originalLogger.getLogger(logPath);

        try{

            for (int yearID = 0; yearID < (systemYear - 1950 + 1); yearID++){
                // 変数定義
                int targetYear = (systemYear - yearID); // データ取得対象年度
                Elements targetYearElements = linkAllyaerTable.get(0).select("th"); // YearID年度とリンクを入れるリスト
                Elements teamElements = linkAllyaerTable.get(0).select("td").get(0).select("a"); // YearID年度、全チームの名前とリンクを入れるリスト

                // debug
                if (yearID == 1){
                    break;
                }

                Path outputPath = Paths.get("c:\\pleiades\\workspace\\ScrapingTest\\output\\" + targetYear);

                try{
                  if (! (Files.exists(outputPath))){
                      Files.createDirectory(outputPath);
                  }
                }catch(IOException e){
                  System.out.println(e);
                }

                // リンクとチーム名を持ったaタグを取得
                for (Element targetTeam: teamElements){
                    String teamName = targetTeam.ownText().replace(" ", "");
                    String teamLink = targetTeam.absUrl("href");

                    Document teamHtml = (Document) Jsoup.connect(teamLink).get(); // year年の選手一覧のページ
                    Elements playersStutsTable = teamHtml.select("#team_batting tbody tr");  // 各チーム、全選手の情報が入ったテーブル

                    try {

                        for(Element targetPlayerElements : playersStutsTable){
                            EntityManager em = DBUtil.createEntityManager();

                            // Modelを生成
                            StutsCen stutsCen = new StutsCen();

                            Elements targetPlayer_tds = targetPlayerElements.select("td"); // 選手のtdを格納するリスト

                            stutsCen.setYEAR( Integer.parseInt( targetYearElements.select("a").get(yearID).ownText() ) );
                            stutsCen.setPLAYERNAME(targetPlayer_tds.get(0).select("a").get(0).ownText());

                            String strAVG;
                            if ( targetPlayer_tds.get(15).ownText() == "" || targetPlayer_tds.get(15).ownText() == null || targetPlayer_tds.get(15).ownText().isEmpty() ){
                                strAVG = "0";
                            }else {
                                strAVG = targetPlayer_tds.get(15).ownText();
                                        if ( strAVG.startsWith(".") ) {
                                            strAVG = "0" + strAVG;
                                        }
                            }
                            stutsCen.setAVG( Double.valueOf( strAVG ) );

                            stutsCen.setHOMERUN( Integer.parseInt( targetPlayer_tds.get(9).ownText() ) );
                            stutsCen.setRBI( Integer.parseInt( targetPlayer_tds.get(10).ownText() ) );

                            String strOPS;
                            if ( targetPlayer_tds.get(18).ownText() == "" || targetPlayer_tds.get(18).ownText() == null || targetPlayer_tds.get(18).ownText().isEmpty()){
                                strOPS = "0";
                            }else {
                                strOPS = targetPlayer_tds.get(18).ownText();
                                        if ( strOPS.startsWith(".") ) {
                                            strOPS = "0" + strOPS;
                                        }
                            }
                            stutsCen.setOPS( Double.valueOf( strOPS ) );

                            stutsCen.setSTOLENBASE( Integer.parseInt( targetPlayer_tds.get(11).ownText() ) );

                            logger.log(Level.INFO, "出力成功 : " + teamName + " : " + stutsCen.getPLAYERNAME());
                            System.out.println(strAVG + " : " + strOPS);

                            // データベースに保存
                            em.getTransaction().begin();
                            em.persist(stutsCen);
                            em.getTransaction().commit();
                            em.close();
                            logger.log(Level.INFO, stutsCen.getID().toString());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



        }catch(IOException e){
            System.out.println(e);
          }
    }
}
