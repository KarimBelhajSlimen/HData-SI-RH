(function() {

  var module = angular.module('yaMetrika',
    []
  );

  module.provider('yaMetrika', function() {
    var counter = null;

    this.setCounter = function(ycounter) {
      counter = ycounter;
    };

    this.$get = function($log) {
      return {
        reachGoal: function(goal, params) {
          if (counter) {
            counter.reachGoal(goal, params);
            $log.debug('yaMetrika goal reached: ' + goal);
          } else {
            $log.error('yaMetrika counter is not configured');
          }
        }
      }
    };
  });

  module.directive('yaMetrika', function(yaMetrika) {
    return {
      link: function($scope, $element, attrs) {
        var params = $scope.$eval(attrs.yaMetrika);

        var event = params.event || 'click';
        var goal = params.goal;
        delete params.event;

        if (goal) {
          $element.bind(event, function() {
            yaMetrika.reachGoal(goal, params);
          });
        }
      }
    }
  });

}());