appServices.factory('EventsService', function($log, $resource) {

	var eventsService = {};
	eventsService.getAll = function () {
        var eventResource = $resource('events', {}, {
            query: {method: 'GET', params: {}, isArray: true}
        });
        return eventResource.query();
    };

	return eventsService;
});

