(function() {
    const originalFetch = window.fetch;
    const originalXHR = XMLHttpRequest.prototype.send;

    function interceptFetch() {
        window.fetch = async function(...args) {
            // console.log('Intercepted fetch:', args);
            return originalFetch.apply(this, args);
        };
    }
    function interceptXHR() {
        XMLHttpRequest.prototype.send = function(body) {
            // console.log('Intercepted XMLHttpRequest:', body);
            originalXHR.call(this, body);
        };
    }

    // Intercept fetch and XHR initially
    interceptFetch();
    interceptXHR();

    // Use MutationObserver to reapply interceptors if they are overwritten
    const observer = new MutationObserver(() => {
        if (window.fetch !== originalFetch) {
            interceptFetch();
        }
        if (XMLHttpRequest.prototype.send !== originalXHR) {
            interceptXHR();
        }
    });

    observer.observe(document, {
        subtree: true,
        childList: true,
    });
    console.log("加载成功！")
})();
