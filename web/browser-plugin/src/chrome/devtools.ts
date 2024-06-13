const CD = chrome.devtools;
// Chrome DevTools Extension中不能使用console.log
const log = (...params: any) => CD.inspectedWindow.eval(`console.log(...${JSON.stringify(params)})`);

log('devtools.js log........');
chrome.devtools.network.onRequestFinished.addListener(
async (...args: any) => {
    try {
        const [{
            // 请求的类型，查询参数，以及url
            request: {method, queryString, url},
            response: {bodySize, status},
            // 该方法可用于获取响应体
            getContent,
        }] = args;

        log(method, queryString, url);
        log(bodySize, status);

        // 将callback转为await promise
        // warn: content在getContent回调函数中，而不是getContent的返回值
        const content = await new Promise((res, rej) => getContent(res));
        log('response=   ' + content);
        // 调用background.js中的函数
        var bg = chrome.extension.getBackgroundPage();
        // bg.sendRequest(url);

    } catch (err: any) {
        log(err.stack || err.toString());
    }
});
