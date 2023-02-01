myapp.directive('access', [
    'AuthSharedService',
    function (AuthSharedService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var roles = attrs.access.split(',');
                if (roles.length > 0) {
                    if (AuthSharedService.isAuthorized(roles)) {
                        element.removeClass('hide');
                    } else {
                        element.addClass('hide');
                    }
                }
            }
        };
    }])
    .directive('ngReallyClick', [function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                element.bind('click', function() {
                    var message = attrs.ngReallyMessage;
                    if (message && confirm(message)) {
                        scope.$apply(attrs.ngReallyClick);
                    }
                });
            }
        }
    }])
    .directive('afterRender', ['$timeout', function ($timeout) {
        var def = {
            restrict: 'A',
            terminal: true,
            transclude: false,
            link: function (scope, element, attrs) {
                $timeout(scope.$eval(attrs.afterRender), 0);  //Calling a scoped method
            }
        };
        return def;
    }]);