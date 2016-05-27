import {RouterHelper} from '../dvbModules/router/route-helper-provider';
import {IState} from 'angular-ui-router';
import ListResourceRS from './service/listResourceRS.rest';
import IRootScopeService = angular.IRootScopeService;
import ITimeoutService = angular.ITimeoutService;

appRun.$inject = ['angularMomentConfig', 'RouterHelper', 'ListResourceRS', '$rootScope', 'hotkeys', '$timeout'];

/* @ngInject */
export function appRun(angularMomentConfig: any, routerHelper: RouterHelper, listResourceRS: ListResourceRS, $rootScope: IRootScopeService, hotkeys: any, $timeout: ITimeoutService) {
    routerHelper.configureStates(getStates());
    angularMomentConfig.format = 'DD.MM.YYYY';
    // dieser call macht mit tests probleme, daher wird er fuer test auskommentiert
    if (ENV !== 'test') {
        listResourceRS.getLaenderList();  //initial aufruefen damit cache populiert wird
    }
    $rootScope.$on('$viewContentLoaded', function () {
        angular.element('html, body').animate({scrollTop: 0}, 200);
        //    oder so  $anchorScroll('top') mit einem <div id="top">;
    });
    // Wir meochten eigentlich ueberall mit einem hotkey das formular submitten koennen
    //https://github.com/chieffancypants/angular-hotkeys#angular-hotkeys-
    hotkeys.add({
        combo: 'ctrl+shift+x',
        description: 'Press the last button with style class .next',
        callback: function () {
            $timeout(() => angular.element('.next').last().click());
        }
    });

}

function getStates(): IState[] {
    return [
        /* Add New States Above */
    ];
}