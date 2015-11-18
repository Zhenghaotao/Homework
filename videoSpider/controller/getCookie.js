/**
 * Created by Administrator on 2015/10/13.
 */
var config = require('../config.js');
var http = require('http');
var request = require('request');
var fs = require('fs');
var iconv = require('iconv-lite');
//var BufferHelper = require('bufferhelper');
//var gbk = require('gbk');

var account = config.account,
    refer = config.referUrl,
    entrance = config.entranceUrl;

var parseHost = function(){
    var pattern = /^(?:(\w+):\/\/)?(?:(\w+):?(\w+)?@)?([^:\/\?#]+)(?::(\d+))?(\/[^\?#]+)?(?:\?([^#]+))?(?:#(\w+))?/;
    var ret = pattern.exec(entrance);
    return ret[4];
}

var saveCookie = function(request, result){
    var pattern = /successful/;
    if(pattern.test(result)){
        var cookies = request.headers['set-cookie'];

        fs.open("cookie.txt", "a", 0644, function(e, fd){
            for(var i=0; i<cookies.length; i++){
                fs.write(fd, cookies[i].toString()+'\r\n', function(e){
                    if(e){
                        throw e;
                    }
                    if(i == cookies.length - 1){
                        fs.closeSync(fd);
                    }
                });
            }
        });
    }
}

var getCookie = function(){
    var field = {
        "username" : account.user,
        "password" : account.password,
        "login-form-type" : "pwd"
    };

    var host = parseHost();
    var origin = "http://" + host;

    var param = "";
    for(var attr in field){
        param += attr + "=" + encodeURI(field[attr]) + "&";
    }
    param = param.slice(0, -1);

    var headers = {
        "Host" : host,
        "Connection" : "keep-alive",
        "Content-Type" : "application/x-www-form-urlencoded",
        "Content-Length" : param.length,
        "Cache-Control" : "max-age=0",
        "Accept" : "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Origin" : origin,
        "User-Agent" : "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.12 Safari/537.31",
        "Referer" : refer,
        "Accept-Encoding" : "*",
        "Accept-Language" : "zh-CN,zh;q=0.8",
        "Accept-Charset" : "GBK,utf-8,GB2312;q=0.7,*;q=0.3"
    };

    var option = {
        method : "POST",
        host : host,
        port : 80,
        path : "/pkmslogin.form",
        headers : headers
    };

    var req = http.request(option, function(feedback){
        if(feedback.statusCode == 200){
            var result = "";
            //feedback.setEncoding("gb2312");
            feedback.on('data', function(chunk){
                //result += iconv.encode(iconv.decode(data, 'GBK'), 'GBK');
                result += chunk;
            }).on('end', function(){
                //result = iconv.decode(result, 'gbk');
                saveCookie(feedback, result);
                //fs.open("test.html", "a", 0644, function(e, fd){
                //    if(e){
                //        throw e;
                //    }
                //    fs.write(fd, result, function(e){
                //        if(e){
                //            throw e;
                //        }
                //        fs.closeSync(fd);
                //    })
                //});
            });
        }else{
            console.log("error");
        }
    });
    req.write(param);
    req.end();
}

module.exports = getCookie;

