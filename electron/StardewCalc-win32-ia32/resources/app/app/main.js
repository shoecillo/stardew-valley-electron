


var stardewApp = angular.module('stardew', ["ui.bootstrap","ngCookies"]);
stardewApp.constant("isFrontDev",isFrontendDev());
stardewApp.factory('requestHeadersInterceptor',requestHeadersInterceptor);
stardewApp.config(['isFrontDev','$httpProvider',function(isFrontDev,$httpProvider){

	if(isFrontendDev()) {
        $httpProvider.interceptors.push('requestHeadersInterceptor');
    }
}]);



function config(isFrontDev,$httpProvider)
{
	if(isFrontendDev) {
        $httpProvider.interceptors.push('requestHeadersInterceptor');
    }
}


function isFrontendDev() {
    return (window.location.href.indexOf('localhost:300') >= 0 || window.location.href.indexOf('shoe011.dev.com:300') >= 0);
}


/** @ngInject **/
function requestHeadersInterceptor()  {
    return {
        request: function request(config) {
            var index = config.url.indexOf('api/stardew/')
            if(index >= 0) {
                config.method = "GET";
                config.url = config.url + ".json";
            }
            return config;
        }
    };
};
