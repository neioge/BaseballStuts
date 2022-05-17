package scraping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import log.OriginalLogger;

public class Scraping {
    public void GetDataFromURL (String targetURL) throws IOException{
        // 変数定義
        int systemYear = YearMonth.now().getYear(); // 今年
        Document allYearHtml = (Document) Jsoup.connect(targetURL).get(); // Document形式のスクレイピング対象のページ
        Elements linkAllyaerTable = allYearHtml.select("#lg_history tbody");  // 毎年度、全チームの情報が入ったテーブル

        // ロガー
        String logPath = "C:\\pleiades\\workspace\\BaseballStuts\\Log\\";
        OriginalLogger originalLogger = new OriginalLogger();
        Logger logger = originalLogger.getLogger(logPath);

        try{

            for (int yearID = 0; yearID < (systemYear - 1950 + 1); yearID++){
                // 変数定義
                int targetYear = (systemYear - yearID); // データ取得対象年度
                Elements teamElements = linkAllyaerTable.get(0).select("td").get(0).select("a"); // YearID年度、全チームの名前とリンクを入れるリスト

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
                        File outputFile = new File("c:\\pleiades\\workspace\\ScrapingTest\\output\\" + targetYear + "\\"  + teamName + ".csv");
                        FileWriter outputFileWriter = new FileWriter(outputFile);
                        outputFileWriter.write("Name" + "," + "Average" + "," + "Homerun" + "," + "RBI" + "," + "OPS" + "," + "StolenBase" + "\r\n");
                        for(Element targetPlayerElements : playersStutsTable){
                            Elements targetPlayer_tds = targetPlayerElements.select("td"); // 選手のtdを格納するリスト
                            String targetPlayerName = targetPlayer_tds.get(0).select("a").get(0).ownText();
                            String targetPlayerAvg = "0" + targetPlayer_tds.get(15).ownText();
                            String targetPlayerHR = targetPlayer_tds.get(9).ownText();
                            String targetPlayerRBI = targetPlayer_tds.get(10).ownText();
                            String targetPlayerOPS = "0" + targetPlayer_tds.get(18).ownText();
                            String targetPlayerSB = targetPlayer_tds.get(11).ownText();
                            outputFileWriter.write(targetPlayerName + "," + targetPlayerAvg + "," + targetPlayerHR + "," + targetPlayerRBI + "," + targetPlayerOPS + "," + targetPlayerSB + "\r\n");

                            logger.log(Level.INFO, "出力成功" + targetPlayerName);
                        }
                        outputFileWriter.close();

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
