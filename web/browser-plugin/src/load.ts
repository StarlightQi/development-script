(function () {
    function sendDataToExtension(key: string, value: string) {
        // @ts-ignore
        chrome.runtime.sendMessage({type: 'storeData', key, value}, (response: any) => {
            console.log('Response from extension:', response);
            initShareData(response)
            // 可以在这里处理返回的数据，例如更新页面内容
        });
    }

    // window.onload = () => {
    const exampleKey = 'exampleKey';
    const exampleValue = ''; // 可以设置为空或者其他值进行测试
    sendDataToExtension(exampleKey, exampleValue);
    // };

    // 初始化数据
    function initShareData(data: any) {
        if (data) {
            // @ts-ignore
            window.localStorage.setItem("TestData", JSON.stringify(data))
        }
    }


    const script = document.createElement('script');
    // @ts-ignore
    script.src = chrome.runtime.getURL('browser.umd.js');
    script.onload = function () {
        // @ts-ignore
        this.remove();
    };
    (document.head || document.documentElement).appendChild(script);
})();

