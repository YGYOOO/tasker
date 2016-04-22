tmod = angular.module('Things.Players', ['ngRoute', 'ngResource', 'Session']);

tmod.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/players', {
    templateUrl: 'resources/views/players/players.html',
    controller: 'Things.Controller'
  });
}]);

tmod.controller('Things.Controller', ['$scope', '$resource', 'Session.Service', function($scope, $resource, Session) {
   var Things = $resource('api/users/:uid/things/:tid', {});
   
   $scope.things = Things.query( { uid : Session.user() ? Session.user().id : null } );
   $scope.newPlayer = {};
   
   $scope.delete = function( thing ) {
      Things.delete( { uid : Session.user().id, tid : thing.id }, function() {
         $scope.things = $scope.things.filter( function( t ) { return t.id !== thing.id; } );
      } )
   }

   $scope.addPlayer = function() {
      console.log( 'creating' );
      Things.save( { uid : Session.user().id },
                   { name : $scope.newPlayer.name,
                     value : $scope.newPlayer.position },
                   function( response ) {
                      $scope.newPlayer = { };
                      $scope.things.push( response );
                   } );
   }
}]);
