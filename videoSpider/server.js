/**
 * Created by Administrator on 2015/10/23.
 */
var http = require('http');
var getVideo = require('./controller/getVideo.js');

http.createServer(function (req, res){
	res.writeHead(200, {'Content-Type' :'text/plain'});
	var list = getVideo.getVideoReference();
	res.end(list.toString());
}).listen(8888, "127.0.0.1");
console.log('Server running');
