'use strict';

/* Controllers */

var appControllers = angular.module('yuccalight.controllers', []);

appControllers.controller('GlobalCtrl', [ '$scope', '$translate', function($scope, $translate) {
	
	$scope.changeLanguage = function(langKey) {
		$translate.use(langKey);
	};


} ]);

