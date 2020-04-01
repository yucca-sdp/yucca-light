
appControllers.controller('EventsController', [ '$scope', "$log", 'EventsService', function($scope, $log, EventsService) {
	 var pippo = EventsService.getAll();
	    $scope.events = pippo;
} ]);
