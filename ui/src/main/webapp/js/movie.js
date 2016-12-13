moviezApp.controller("LoadMoviePageCont", function($scope, $routeParams, $http,
		$sce) {
	
	$scope.modalID = 'myModal0';
	$scope.posIndex = 0;
	$scope.negIndex = 0;
	$scope.commentWindowSize = 5;
	$http({
		method : 'GET',
		url : '/movieplot',
		headers : {
			'Content-Type' : 'application/json'
		}
	}).success(function(data) {
		console.log('successfull');
		$scope.movieobj = data;
		$scope.commentType = 'overall';
		$scope.positiveReviews = $scope.movieobj.overall.positiveReviews;
		$scope.negativeReviews = $scope.movieobj.overall.negativeReviews;
		
	});
	
	$scope.posNext = function() {
		if($scope.positiveReviews.length > $scope.posIndex + $scope.commentWindowSize) {
			console.log("calling posnext with" + $scope.posIndex + $scope.commentWindowSize);
			$scope.posIndex = $scope.posIndex + $scope.commentWindowSize;
		}
	}
	
	$scope.posPrev = function() {
		if($scope.posIndex - $scope.commentWindowSize >= 0){
			$scope.posIndex = $scope.posIndex - $scope.commentWindowSize; 
		}
	}
	
	$scope.negNext = function() {
		if($scope.negativeReviews.length > $scope.negIndex + $scope.commentWindowSize) {
			console.log("calling posnext with" + $scope.negIndex + $scope.commentWindowSize);
			$scope.negIndex = $scope.negIndex + $scope.commentWindowSize;
		}
	}
	
	$scope.negPrev = function() {
		if($scope.negIndex - $scope.commentWindowSize >= 0){
			$scope.negIndex = $scope.negIndex - $scope.commentWindowSize; 
		}
	}
	
	$scope.buldDefaultList = function(type) {
		console.log('typs is' + type);
		if (type == 'songAndTrailers') {
			$scope.subtype = 'songAndTrailers';
			$scope.list = $scope.movieobj.songAndTrailers;
		} else if (type == 'videoReviews') {
			$scope.subtype = 'videoReviews';
			$scope.list = $scope.movieobj.videoReviews;
		} else if (type == 'interviewAndEvents') {
			$scope.subtype = 'interviewAndEvents';
			$scope.list = $scope.movieobj.interviewAndEvents;
		}
	}

	$scope.getBuildDefaultList = function() {
		return $scope.list;
	}

	$scope.getSongAndTrailerImage = function(index) {
		// console.log('index found is ' + index);
		return $scope.list[index].replace('embed', 'vi').replace('www', 'img')
				.replace(new RegExp('$'), '/0.jpg');
	}

	$scope.initializeModal = function(subtypeStr, index) {
		console.log('new modal id is ' + 'myModal' + index + subtypeStr);
		$scope.modalID = 'myModal' + subtypeStr + index;
		$scope.embeddedURL = $sce.trustAsResourceUrl($scope.list[index]
				+ '?autoplay=1');
	}

	$scope.clearEmbeddedURL = function() {
		$scope.embeddedURL = $sce.trustAsResourceUrl('http://www.example.com');
	}

	$scope.fillReviewType = function(reviewtype) {
		$scope.posIndex = 0;
		$scope.negIndex = 0;
		if (reviewtype == 'overall') {
			$scope.commentType = 'overall';
			$scope.positiveReviews = $scope.movieobj.overall.positiveReviews;
			$scope.negativeReviews = $scope.movieobj.overall.negativeReviews;
		} else if (reviewtype == 'story') {
			$scope.commentType = 'story';
			$scope.positiveReviews = $scope.movieobj.story.positiveReviews;
			$scope.negativeReviews = $scope.movieobj.story.negativeReviews;
		} else if (reviewtype == 'acting') {
			$scope.commentType = 'acting';
			$scope.positiveReviews = $scope.movieobj.acting.positiveReviews;
			$scope.negativeReviews = $scope.movieobj.acting.negativeReviews;
		} else if (reviewtype == 'direction') {
			$scope.commentType = 'direction';
			$scope.positiveReviews = $scope.movieobj.direction.positiveReviews;
			$scope.negativeReviews = $scope.movieobj.direction.negativeReviews;
		} else if (reviewtype == 'music') {
			$scope.commentType = 'music';
			$scope.positiveReviews = $scope.movieobj.music.positiveReviews;
			$scope.negativeReviews = $scope.movieobj.music.negativeReviews;
		}
	}
	
	$scope.isClicked = true;
	$scope.isNotClicked = false;

	$scope.commentClick = function()
	{
		console.log("Commit Column Clicked");
		$scope.isClicked = false;
		$scope.isNotClicked = true;
	}
	$scope.submitComment = function()
	{
		$scope.isClicked = true;
		$scope.isNotClicked= true;		
	}

});
