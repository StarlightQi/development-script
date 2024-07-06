// @ts-ignore
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.type === 'storeData') {
        const {key, value} = message;
        if (!value) {
            // 如果值为空，尝试从存储中获取之前的值
            // @ts-ignore
            chrome.storage.local.get(key, (result) => {
                const storedValue = result[key];
                sendResponse({key, value: storedValue});
            });
        } else {
            // 如果值不为空，存储新值并返回
            // @ts-ignore
            chrome.storage.local.set({[key]: value}, () => {
                console.log('Data stored:', {[key]: value});
                sendResponse({key, value});
            });
        }
        return true; // Indicate that you will send a response asynchronously
    }
});
