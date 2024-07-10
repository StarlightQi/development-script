const express = require('express');
const httpProxy = require('http-proxy');
const {HttpsProxyAgent} = require('https-proxy-agent');
const app = express();
app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
    next();
});
const proxy = httpProxy.createProxyServer({
    target: 'https://developers.google.com/',
    changeOrigin: true,
    secure: false,
    agent: new HttpsProxyAgent('http://127.0.0.1:7890')
});

// HttpsProxyAgent('http://username:password@127.0.0.1:7890')

app.use('/node-api', (req, res) => {
    proxy.web(req, res, {
            target: 'https://developers.google.com/',
            headers: {
                'X-Forwarded-For': '127.0.0.1:7890'
            },
            proxyTimeout: 5000,
            timeout: 5000,
            proxyReqOptDecorator: function (proxyReqOpts, srcReq) {
                // 添加自定义请求头
                proxyReqOpts.headers['Custom-Header-1'] = 'CustomValue1';
                proxyReqOpts.headers['Custom-Header-2'] = 'CustomValue2';
                return proxyReqOpts;
            }
        },
        (err) => {
            res.status(500).send('Proxy error');
        });
});

module.exports = app;
