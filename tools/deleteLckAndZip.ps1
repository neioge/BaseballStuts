# ���s�ł��Ȃ��Ƃ�
# https://qiita.com/ponsuke0531/items/4629626a3e84bcd9398f

# �X�N���v�g���s�p�X�ړ�
$path = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $path

# lck�t�@�C���̍폜
Remove-Item -Path * -Filter *.lck 

if(Test-Path .\allLog.zip){
    # ���ɂ���Zip�ɒǉ�����B
    Compress-Archive -Path .\*.log -DestinationPath .\allLog.zip -Update
}else{
    # �܂Ƃ߂ă��O�����k
    Compress-Archive -Path ./*.log -DestinationPath allLog.zip
}

# log�t�@�C���̍폜
Remove-Item -Path * -Filter *.log
