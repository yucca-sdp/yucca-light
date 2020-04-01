'use strict';

/* Directives */
var appDirectives = angular.module('yuccalight.directives', []);

appDirectives.directive('appVersion', [ 'version', function(version) {
	return function(scope, elm, attrs) {
		elm.text(version);
	};
} ]);

appDirectives.directive('mainNavbar', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/common/main-navbar.html?'
	};
});

appDirectives.directive('mainFooter', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/common/main-footer.html?'
	};
});


appDirectives.directive('consoleView', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/console/console.html?'
	};
});

appDirectives.directive('queueList', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/console/queue-list.html?'
	};
});

appDirectives.directive('streamList', function() {
	return {
		restrict : 'E',
		templateUrl : 'partials/console/stream-list.html?'
	};
});

appDirectives.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});


appDirectives.directive('scrollOnClick', function() {
	return {
	    restrict: 'A',
	    link: function(scope, $elm, attrs) {
	    	var idToScroll = attrs.href;
	    	console.log("idToScroll",idToScroll);
	    	$elm.on('click', function() {
	    		var $target;
	    		if (idToScroll && idToScroll!='javascript:void(0)') {
	    			$target = $(idToScroll);
	    		} else {
	    			$target = $elm;
	    		}
	    		console.debug("target ",$target);
	    		$("body").animate({scrollTop: $target.offset().top}, "slow");
	    	});
	    }
	 };
});



