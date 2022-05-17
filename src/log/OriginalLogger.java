
// ########################################
// 目的：Java標準ライブラリLoggerを、Handlerを追加し、プロジェクト固有のログフォルダが指定された状態で返すためのクラスです。
// オブジェクト化時の引数：ログフォルダ
// メソッド：ロガーを返すメソッド
// ########################################

package log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class OriginalLogger {

    // フィールド
    private String timeForLog;

    // コンストラクタ
    public OriginalLogger(){
        Date dateObj = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        this.timeForLog = format.format( dateObj );
    }

    // ロガーを返すメソッド
    public Logger getLogger(String logHomePath){
        // Loggerインスタンスの生成
        Logger logger = Logger.getLogger("sample log");

        File logFile = new File(logHomePath + this.timeForLog + "BaseballStuts.log");
        String logPath = logFile.getAbsolutePath();

        // Handlerクラスのインスタンス生成のときの例外処理のためのtry-catch構文
        try{
            // Handlerインスタンスを生成
            Handler handler = new FileHandler(logPath);
            // LoggerにHandlerを登録
            logger.addHandler(handler);
        }catch(IOException e){}

        return logger;
    }
}
