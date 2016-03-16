(function () {
    'use strict';

    angular
        .module('ebeguWeb.core', [
            /* Angular modules */
            'ngAnimate',
            'ngSanitize',
            'ngMessages',
            'ngAria',
            'ngCookies',
            /* shared DVBern modules */
            'dvbAngular.router',
            /* 3rd-party modules */
            'ui.bootstrap',
            'angularMoment',
            'smart-table',
            'pascalprecht.translate'
        ]);
})();
