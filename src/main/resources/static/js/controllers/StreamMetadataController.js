appControllers.controller('StreamMetadataController', [
		'$scope',
		"$log",
		'StreamMetadataService',
		function($scope, $log, StreamMetadataService) {
			$scope.streamMetadataList = [];
			console.log("StreamMetadataController - getAll", StreamMetadataService.getAll().$promise);

			$scope.streamMetadataList = StreamMetadataService.getAll();

			$scope.filteredStreamsList = [];
			$scope.codeFilter = null;
			$scope.currentPage = 1;
			$scope.pageSize = 10;
			$scope.totalItems = $scope.streamMetadataList.length;
			$scope.predicate = '';
			$scope.showLoading = false;

			$scope.searchCodeFilter = function(stream) {
				var keyword = new RegExp($scope.codeFilter, 'i');
				return !$scope.codeFilter || keyword.test(stream.streamCode)
						|| keyword.test(stream.streamName);
			};

			$scope.$watch('codeFilter', function(newCode) {
				$scope.currentPage = 1;
				$scope.totalItems = $scope.filteredStreamsList.length;
			});

		} ]);
