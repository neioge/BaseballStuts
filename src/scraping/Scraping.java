package scraping;

import java.io.IOException;
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

        // データベースマネージャー
        EntityManager em = DBUtil.createEntityManager();


        try{

            // 年度で繰り返し
            for (int yearCounter = 1950; yearCounter < systemYear; yearCounter++){
                // 変数定義
                int targetYearID = systemYear - yearCounter; // 取得対象年の順番

                // 当サイトの表は15年ごとにヘッダーを挟んでいる。飛ばすための処理。
                String checkYear = linkAllyaerTable.get(0).select("tr").get(targetYearID).select("th").get(0).ownText();
                if ( checkYear.equals("Year") ){
                    continue;
                 }

                Elements targetYearElements = linkAllyaerTable.get(0).select("tr").get(targetYearID).select("th"); // YearID年度とリンクを入れるリスト
                Elements teamElements = linkAllyaerTable.get(0).select("tr").get(targetYearID).select("td").get(0).select("a"); // YearID年度、全チームの名前とリンクを入れるリスト

                // チームで繰り返し
                for (Element targetTeam: teamElements){

                    // チーム名とリンクを取得
                    String teamName = targetTeam.ownText().replace(" ", "");
                    String teamLink = targetTeam.absUrl("href");

                    // チームのリンクをもとにチーム単位のサイト情報を取得
                    Document teamHtml = (Document) Jsoup.connect(teamLink).get(); // year年の選手一覧のページ
                    Elements playersStutsTable = teamHtml.select("#team_batting tbody tr");  // 各チーム、全選手の情報が入ったテーブル

                    try {

                        // 選手で繰り返し
                        for(Element targetPlayerElements : playersStutsTable){

                            // Modelを生成
                            StutsCen stutsCen = new StutsCen();

                            Elements targetPlayer_tds = targetPlayerElements.select("td"); // 選手のtdを格納するリスト

                            stutsCen.setYEAR( Integer.parseInt( targetYearElements.select("a").get(0).ownText() ) );
                            stutsCen.setPLAYERNAME(targetPlayer_tds.get(0).select("a").get(0).ownText());
                            String strAVG = "";
                            if ( targetPlayer_tds.get(15).ownText() == "" || targetPlayer_tds.get(15).ownText() == null || targetPlayer_tds.get(15).ownText().isEmpty() ){
                                strAVG = "0";
                            }else {
                                        if ( targetPlayer_tds.get(15).ownText().startsWith(".") ) {
                                            strAVG = "0" + targetPlayer_tds.get(15).ownText();
                                        }else{
                                            strAVG = targetPlayer_tds.get(15).ownText();
                                        }
                            }
                            stutsCen.setAVG( Double.valueOf( strAVG ) );
                            stutsCen.setHOMERUN( Integer.parseInt( targetPlayer_tds.get(9).ownText() ) );
                            stutsCen.setRBI( Integer.parseInt( targetPlayer_tds.get(10).ownText() ) );
                            String strOPS = "";
                            if ( targetPlayer_tds.get(18).ownText() == "" || targetPlayer_tds.get(18).ownText() == null || targetPlayer_tds.get(18).ownText().isEmpty()){
                                strOPS = "0";
                            }else {
                                        if ( targetPlayer_tds.get(18).ownText().startsWith(".") ) {
                                            strOPS = "0" + targetPlayer_tds.get(18).ownText();
                                        }else{
                                            strOPS = targetPlayer_tds.get(18).ownText();
                                        }
                            }
                            stutsCen.setOPS( Double.valueOf( strOPS ) );
                            stutsCen.setSTOLENBASE( Integer.parseInt( targetPlayer_tds.get(11).ownText() ) );

                            // データベースに保存
                            em.getTransaction().begin();
                            em.persist(stutsCen);
                            em.getTransaction().commit();

                            logger.log(Level.INFO, "出力成功 : " + teamName + " : " + stutsCen.getPLAYERNAME());
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
        em.close();
    }
}
