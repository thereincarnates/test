
moviezApp.controller(
				'myCtrl',
				function($scope, $sce) {
					console.log('log me');
					console.log($scope.embeddedURL)

					$scope.initialize = function() {
						console.log('calling me');
						$scope.embeddedURL = $sce
								.trustAsResourceUrl('http//movieztalk.com');
						$scope.embeddedURL1 = $sce
								.trustAsResourceUrl('https://www.youtube.com/embed/k99-vMPh3-A?autoplay=1');
					}

					$scope.toggle = function() {
						$scope.embeddedURL1 = $sce
								.trustAsResourceUrl('http//movieztalk.com');
						console.log($scope.embeddedURL);
					}
				});

moviezApp.controller("loadMoviePlot",function($scope,$routeParams,$http)
		 { 
	console.log('calling loadMoviePlot');

		    $http({
		          method: 'GET',
		          url: '/ui/movieplot',
		          headers: {'Content-Type': 'application/json'}
		        }).success(function (data) 
		          {
		        	console.log('successfull');
		            $scope.movieobj=data;
		            
		          });
		 });