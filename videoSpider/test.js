var http = require('http');
var url = require('url').parse('http://www.baidu.com/');
var iconv = require('iconv');
var BufferHelper = require('bufferhelper');
var fs = require('fs');

http.get(url,function(res){
    var bufferHelper = new BufferHelper();
    res.on('data', function (chunk) {
        bufferHelper.concat(chunk);
    });
    res.on('end',function(){
        //var content = iconv.decode(bufferHelper.toBuffer(),'GBK');
        // var convert = new iconv('GB18030', 'UTF-8');
        // var utf8_buffer = convert(bufferHelper.toBuffer());

        fs.open("baidu.html", "a", 0644, function(e, fd){
            if(e){
                throw e;
            }
            fs.write(fd, bufferHelper.toBuffer().toString(), function(e){
                if(e){
                    throw e;
                }
                fs.closeSync(fd);
            })
        })
    });
})