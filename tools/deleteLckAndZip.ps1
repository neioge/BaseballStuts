# 実行できないとき
# https://qiita.com/ponsuke0531/items/4629626a3e84bcd9398f

# スクリプト実行パス移動
$path = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $path

# lckファイルの削除
Remove-Item -Path * -Filter *.lck 

if(Test-Path .\allLog.zip){
    # 既にあるZipに追加する。
    Compress-Archive -Path .\*.log -DestinationPath .\allLog.zip -Update
}else{
    # まとめてログを圧縮
    Compress-Archive -Path ./*.log -DestinationPath allLog.zip
}

# logファイルの削除
Remove-Item -Path * -Filter *.log
