var app = angular.module('yuccalight', [
  'ngResource',
  'yuccalight.filters',
  'yuccalight.services',
  'yuccalight.directives',
  'yuccalight.controllers',
  'pascalprecht.translate',
  'ui.bootstrap',
  'ngPrettyJson'
]);

app.config(['$translateProvider', function ($translateProvider) {
	// add translation table
	$translateProvider
	.translations('en', translations_en)
	.translations('it', translations_it)
	.preferredLanguage('it');
}]);



//
//
//angular
//    .module('myApp', ['ngResource'])
//    .service('EventsService', function ($log, $resource) {
//        return {
//            getAll: function () {
//                var eventResource = $resource('events', {}, {
//                    query: {method: 'GET', params: {}, isArray: true}
//                });
//                return eventResource.query();
//            }
//        }
//    })    
//    .service('QueueService', function ($log, $resource) {
//        return {
//            getAll: function () {
//                var eventResource = $resource('queue', {}, {
//                    query: {method: 'GET', params: {}, isArray: false}
//                });
//                return eventResource.query();
//            }
//        }
//    })
//    .controller('EventsController', function ($scope, $log, EventsService) {
//        $scope.events = EventsService.getAll();
//    })
//    .controller('QueueController', function ($scope, $log, QueueService) {
//        var pippo = QueueService.getAll();
//        $scope.events = pippo;
//    });
