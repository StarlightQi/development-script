(function() {
    const script = document.createElement('script');
    // @ts-ignore
    script.src = chrome.runtime.getURL('browser.umd.js');
    script.onload = function() {
        // @ts-ignore
        this.remove();
    };
    (document.head || document.documentElement).appendChild(script);
})();
