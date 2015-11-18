/**
 * Created by Administrator on 2015/10/23.
 */
var config = require('../config.js');
var http = require('http');
var request = require('request');
var fs = require('fs');
var iconv = require('iconv-lite');

var getIndexPage = function(){

	var headers = {
		"Host" : "lbxt.gdufs.edu.cn",
		"Proxy-Connection" : "keep-alive",
		"Accept" : "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"User-Agent" : "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.12 Safari/537.31",
		"Referer" : "http://lbxt.gdufs.edu.cn/web/index.asp",
		"Accept-Encoding" : "*",
		"Accept-Language" : "zh-CN,zh;q=0.8",
		"Accept-Charset" : "GBK,utf-8,GB2312;q=0.7,*;q=0.3",
		"Cookie" :  [
			'ASPSESSIONIDCCSRCDTB=DMPJIPHABOKAKPGDMCANPHDP',
			'PD_STATEFUL_5c90029c-bed4-11df-9b57-00237d9f6ff6=lbxt.gdufs.edu.cn',
			'PD-H-SESSION-ID=4_6y0JwdEJQ4ZpEARdiKwaOLfGEYO3b-Anf1lLXN5MsJe3klBg',
			'PD-ID=W1j2ra4buTNf2+p10qTqE1algLawJRJzqkRLde7/ekCjMXRbYRkRUEEqZ0FsesC7or2MA2O15kq0hHTgz5YrmTHeo+Ws+orHVf2x8eN42QW8DRursbEcX1p/o/spwYP6Vw7Iw5mhTksnAAVbojByvsBnCM+mHJHow0TyzZDOPzJU2TFPfbwnFud747xI7mtzlfKGJOjLC6INVpB00yqT3jcHArBrrtYLGe6qSxh6Bg4kRcF9PTet2gGbT6JuRYffTo8NI+Q+IdLVlUErLFpizDAAVPPdgWr/'
		],
		"Upgrade-Insecure-Requests" : 1
	};

	var option = {
		method : "GET",
		host : "lbxt.gdufs.edu.cn",
		path : "/web/VOD/Vod_index.asp",
		headers : headers
	};

	var req = http.request(option, function(feedback){
		if(feedback.statusCode == 200){
			var result = "";
			feedback.on('data', function(chunk){
				result += chunk;
			}).on('end', function(){
				//fs.open("index.html", "w", 0644, function(e, fd){
				//    if(e){
				//        throw e;
				//    }
				//    fs.write(fd, result, function(e){
				//        if(e){
				//            throw e;
				//        }
				//        fs.closeSync(fd);
				//    });
				//});
				getVideoReference(result);
			});
		}else{
			console.log("error");
		}
	});
	req.end();
}

function getVideoReference(){
	var content = fs.readFileSync('./index.html', 'utf8')
	var pattern = /<DIV id=reference>([\s|\S]*)<DIV id=container>/g;
	var refDiv = content.match(pattern);

	var columns = [];

	var topColumn = refDiv[0].match(/<B>([\s|\S]+?)：<\/B>/g);
	var subColumnWrap = refDiv[0].match(/<div style=float: left>([\s|\S]+?)<\/div>/g);
	for(var i=0;i<topColumn.length;i++){
		var parent = {
			name : '',
			children : []
		};
		parent.name = topColumn[i].replace(/(<B>)|(：<\/B>)/g,'');
		var subColumns = subColumnWrap[i].match(/\[([\s|\S]+?)\]/g);
		var subColumnsLink = subColumnWrap[i].match(/href=([\s|\S]+?)   style=/g);
		for(var j=0;j<subColumns.length;j++){
			var child = {
				name : '',
				link : ''
			};
			child.name = subColumns[j].replace(/(\[)|(\])/g,'');
			child.link = subColumnsLink[j].replace(/(href=)|(   style=)/g,'');
			parent.children.push(child);
		}
		columns.push(parent);
	}


	//var subColumn = {
	//	name :
	//};
	////return topColumn;
	//
	//for(var i=0;i<subColumnWrap.length;i++){
	//	//console.log(subColumnWrap[i].match(/\[([\s|\S]+?)\]/g));
	//	subColumn.push(subColumnWrap[i].match(/\[([\s|\S]+?)\]/g));
	//}
	console.log(columns[0].children);
}

module.exports = {
	getIndexPage : getIndexPage,
	getVideoReference : getVideoReference
};