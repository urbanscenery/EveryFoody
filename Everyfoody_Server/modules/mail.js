var nodemailer = require('nodemailer');

module.exports.transport = nodemailer.createTransport({  
    service: 'mail host ex.gmail',
    auth: {
        user: 'sender mail',
        pass: 'login password'
    }
});

module.exports.option = {  
    from: 'sender mail',
    to: 'reciever mail',
    subject: '에브리푸디 사업자 인증요청!',
    html:''
};


module.exports.html1 = '<!DOCTYPE html><html><head><meta charset="utf-8"><title></title></head><body><div style="margin : 20px auto; border: 1px solid #cccccc; width:500px;">'+
		'<div class="title" style="font-size: 30px; text-align: center; margin-top:15px">'+
		'사업자 등록 인증 메일입니다.</div><br><img src="';
module.exports.html2 = '"/><div style="text-align: center">인증을 받기 : ';
module.exports.htmlLoca1 = '<br><br>강서 양천 영등포 구로   : 220.230.114.8:3444/admin/confirm/1/';
module.exports.htmlLoca2 = '<br><br>은평 마포 서대문       : 220.230.114.8:3444/admin/confirm/2/';
module.exports.htmlLoca3 = '<br><br>종로 중구 용산         : 220.230.114.8:3444/admin/confirm/3/';
module.exports.htmlLoca4 = '<br><br>도봉 강북 성북 노원     : 220.230.114.8:3444/admin/confirm/4/';
module.exports.htmlLoca5 = '<br><br>동대문 중랑 성동 광진   : 220.230.114.8:3444/admin/confirm/5/';
module.exports.htmlLoca6 = '<br><br>동작 관악 금천         : 220.230.114.8:3444/admin/confirm/6/';
module.exports.htmlLoca7 = '<br><br>서초 강남              : 220.230.114.8:3444/admin/confirm/7/';
module.exports.htmlLoca8 = '<br><br>강북 송파              : 220.230.114.8:3444/admin/confirm/8/';
module.exports.html3 =	'<br><br><br></div><div style="text-align: center; margin-bottom: 15px;">'+
		'인증을 거절하시려면 : 220.230.114.8:3444/admin/reject/';
module.exports.html4 = '</div></div></body></html>'

