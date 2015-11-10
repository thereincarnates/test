moviezApp.controller("LoadMovieGame",function($scope,$routeParams,$http)
 { 
 	 var success= true;
 	 var movieName ;
	 var compareChar = [" ","A","E","I","O","U",","];
	 var replace;
	 var receivedName = [];
	 var maxChoices = 9;
	 var usedChoices = 0;

	 $http({
			method : 'GET',
			url : '/ui/moviegame',
			headers : {
				'Content-Type' : 'application/json'
			}
		}).success(function(data) {
			console.log(data);
			console.log("Char in success"+compareChar);
            movieName = data.slice();
            $scope.imageno = "" ;
            

			for(var  i =0; i< data.length; i++)
			{
				replace = true;
				console.log("data[i]: "+ data[i]);
				for(var j =0;j<compareChar.length;j++)
				{
					console.log("compareChar[j]: "+ compareChar[j]);
					if(!data[i].localeCompare(compareChar[j]))
					{	
                        console.log("set replace false");

                        replace =  false;
                    }        
			    }		
			    if(replace)
			    {
			    	console.log("set data[i]: to _ "+ data[i]);
					data[i] = "_";
                }

				console.log(data);
			}
            $scope.words=data.join(" ");


           console.log("Recieved Dataa"+ movieName);
		});


         $scope.selectfunction = function(option)
            {
               var userMovie = $scope.words;
		       var movie = movieName.join(" ");
               
               if((usedChoices < maxChoices) && ((userMovie.localeCompare(movie))))
               {
	               
	               replace = true;
	               compareChar.push(option);
	               console.log("Push Char " + compareChar);
	               console.log("movieName "+ movieName );
	               receivedName = movieName.slice();
	               success = true; 
                   validchoice = false;

                   for(var  i =0; i< movieName.length; i++)
                   {
                   	  if(!(option.localeCompare(movieName[i])))
							{

	                          validchoice = true;            
 		                    }        

                   }
                   if(!validchoice)
                   {
                   	    usedChoices =  usedChoices + 1;
                   	    
                   	    $scope.imageno = usedChoices ;

                        console.log("invalid option selected");
                   }
                   if(validchoice)
                   {
		               for(var  i =0; i< receivedName.length; i++)
					   {
					   	    replace = true;
					   	    console.log("receivedName[i]: "+ receivedName[i]);
							for(var j =0;j<compareChar.length;j++)
							{
								console.log("compareChar[j]: "+ compareChar[j]);
								if(!receivedName[i].localeCompare(compareChar[j]))
								{	
		                            console.log("set replace false");
			                        replace =  false;
			                    }        
						    }		
						    if(replace)
						    {
								receivedName[i] = "_";
								console.log("set receivedName[i]: to _ "+ receivedName[i])
			                    success = false;

			                }
							console.log(receivedName);
							
					   }
		               $scope.words=receivedName.join(" ");
		               $scope.won = "";
		                var userMovie = $scope.words;
		                var movie = movieName.join(" ");
		            
		            	if(!(userMovie.localeCompare(movie)))
		            		{
		            			$scope.won="Congratulations!!!! You Won...";
								
							}
				    }
	            }
	            
	           if(usedChoices == maxChoices)
	           {
                     userMovie = $scope.words;
		             movie = movieName.join(" ");
		            
		            if(!(userMovie.localeCompare(movie)))
		            {
		            		$scope.won="Congratulations!!!! You Won...";
								
					}
					else
					{
						$scope.won="SORRY !!!! You Lose...";
					}

			    }

            }

           
 });