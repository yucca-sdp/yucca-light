
appControllers.controller('AttemptsController', [ '$scope', "$log", 'AttemptsService', function($scope, $log, AttemptsService) {
	 var attmps = AttemptsService.getAll();
	    $scope.attempts = attmps;
} ]);
