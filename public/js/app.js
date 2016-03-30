'use strict';
/**
 * Déclaration de l'application FinaxysApp
 */
var FinaxysApp = angular.module('FinaxysApp', [
    // Dépendances du "module"
    'ngRoute'
    //'AppControllers'
]);
/**
 * Configuration du module principal : FinaxysApp
 */
FinaxysApp.config(['$routeProvider',
    function($routeProvider) {
        // Système de routage
        $routeProvider
            .when('/auth', {
                templateUrl: '/assets/partials/authentification.html',
                controller: 'AuthCtrl'
            })
            .when('/home', {
                templateUrl: '/assets/partials/home.html'
            })
            .when('/signup', {
                templateUrl: '/assets/partials/signup.html',
                controller: 'SignCtrl'
            })
            .when('/signinRH', {
                templateUrl: '/assets/partials/signinRH.html',
                controller: 'AuthCtrl2'
            })
            .when('/candidat', {
                templateUrl: '/assets/partials/CandidateForm.html',
                controller: 'CandidatCtrl'
            })
            .when('/profileRH', {
                templateUrl: '/assets/partials/profileForRH.html',
            })
            .when('/profile', {
                templateUrl: '/assets/partials/profile.html',
                controller: 'ProfileCtrl'
            })
            .when('/offres', {
                templateUrl: '/assets/partials/offres.html',
            })
            .when('/entretien', {
                templateUrl: '/assets/partials/entretien.html',
            })
            .when('/acceuil', {
                templateUrl: '/assets/partials/acceuil.html',
            })
            .when('/consulter', {
                templateUrl: '/assets/partials/consulter.html',
            })
            .otherwise({
                redirectTo: '/home'
            });
    }
]);
FinaxysApp.run(function($rootScope) {
    if (localStorage.getItem("session") == "true") {} else {
        $rootScope.session = "false";
        $rootScope.type = "";
        localStorage.setItem("session", $rootScope.session);
        localStorage.setItem("picture", "css/user.png");
        localStorage.setItem("type", $rootScope.type);
        $rootScope.experiences = [];
        $rootScope.formation = [];
        $rootScope.skills = [];
        $rootScope.user = {
            "username": "",
            "password": "",
            "firstname": "",
            "lastname": "",
            "dob": "",
            "address": "",
            "number": "",
            "email": "",
            "linkedin": "",
            "github": "",
            "description": "",
            "experiences": $rootScope.experiences,
            "formation": $rootScope.formation,
            "skills": $rootScope.skills
        };
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        $rootScope.employee = {
            "username": "",
            "password": "",
            "firstname": "",
            "lastname": "",
            "dob": "",
            "address": "",
            "number": "",
            "email": "",
            "linkedin": "",
            "github": "",
            "experiences": $rootScope.experiences,
            "formation": $rootScope.formation,
            "skills": $rootScope.skills
        };
    }
    console.log("fonction run*********************** ");
});
FinaxysApp.controller('AuthCtrl', function($scope, $http, $location, $rootScope) {
    if (localStorage.getItem("session") == "true") $location.path('/candidat');
    // else{
    //     localStorage.setItem("auth", "false");
    //     localStorage.setItem("username", "");
    // }
    $scope.verify = function(username, password) {
        //console.log('{"username":"' + username + '","password":"' + password + '"}');
        $http.post('http://' + server + '/login', '{"username":"' + username + '","password":"' +
            password + '"}').
        success(function(data) {
            $scope.greeting = data;
            console.log(data);
        });
        localStorage.setItem("session", "true");
        localStorage.setItem("type", "candidat");
        $rootScope.session = localStorage.getItem("session");
        $rootScope.type = localStorage.getItem("type");
        $rootScope.user = JSON.parse(localStorage.getItem("user"));
        $rootScope.user.username = username;
        $rootScope.user.password = password;
        //console.log("controller auth  username= " + $rootScope.user.username);
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        //$location.path('/candidat');
    }
});
FinaxysApp.controller('SignCtrl', function($scope, $http, $location, $rootScope) {
    if (localStorage.getItem("session") == "true") $location.path('/candidat');
    // else {
    //     localStorage.setItem("auth", "false");
    //     localStorage.setItem("username", "");
    // }
    $scope.verify = function(username, email, password, cpassword) {
        localStorage.session = "true";
        $rootScope.session = localStorage.getItem("session");
        localStorage.setItem("type", "candidat");
        $rootScope.type = localStorage.getItem("type");
        $rootScope.user = JSON.parse(localStorage.getItem("user"));
        $rootScope.user.username = username;
        $rootScope.user.password = password;
        $rootScope.user.email = email;
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        $location.path('/candidat');
    }
});
FinaxysApp.controller('CandidatCtrl', function($scope, $http, $location, $rootScope) {
    $rootScope.user = JSON.parse(localStorage.getItem("user"));
    $rootScope.firstname = $rootScope.user.firstname;
    $rootScope.lastname = $rootScope.user.lastname;
    $rootScope.dob = $rootScope.user.dob;
    $rootScope.address = $rootScope.user.address;
    $rootScope.number = $rootScope.user.number;
    $rootScope.email = $rootScope.user.email;
    $rootScope.linkedin = $rootScope.user.linkedin;
    $rootScope.github = $rootScope.user.github;
    $rootScope.description = $rootScope.user.description;
    $scope.add_exp = function(year, company, poste, post_description) {
        $rootScope.user = JSON.parse(localStorage.getItem("user"));
        $rootScope.user.experiences.push({
            year: year,
            company: company,
            poste: poste,
            post_description: post_description
        });
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        // Réinitialisation des variables
        $scope.year = '';
        $scope.company = '';
        $scope.poste = '';
        $scope.post_description = '';
    };
    $scope.add_educ = function(year_educ, school, degree, activity) {
        $rootScope.user = JSON.parse(localStorage.getItem("user"));
        $rootScope.user.formation.push({
            year_educ: year_educ,
            school: school,
            degree: degree,
            activity: activity
        });
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        // Réinitialisation des variables
        $scope.year_educ = '';
        $scope.school = '';
        $scope.degree = '';
        $scope.activity = '';
    };
    $scope.add_skill = function(skill) {
        $rootScope.user = JSON.parse(localStorage.getItem("user"));
        $rootScope.user.skills.push({
            skill: skill
        });
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        // Réinitialisation des variables
        $scope.skill = '';
    };
    $scope.images = [];
    $rootScope.images = [];
    //Don't let the same file to be added again
    $scope.removeImage = function(index) {
        $scope.images.splice(index, 1);
    };
    //Add new file to $scope.images
    $scope.addImage = function() {
        console.log("fonction ajouter image*********************** ");
        $scope.images = [];
        var
            reader = new FileReader(),
            $img = $("#img")[0],
            index = $scope.images.length;
        reader.onload = function(e) {
            if ($scope.images.indexOf(e.target.result) > -1) return;
            $scope.images.push(e.target.result);
            if (!$scope.$$phase) $scope.$apply();
            $("#imagePreview" + index).attr('src', e.target.result);
            $scope.uploadImage(index);
        }
        reader.readAsDataURL($img.files[0]);
        $rootScope.images = $scope.images;
        //localStorage.setItem("koala",JSON.stringify($rootScope.images));
        //localStorage.setItem("taswira",$rootScope.images[0]);
        //console.log($rootScope.images[0]);
    };
    $scope.uploadImage = function(index) {
        var res = $scope.images[index];
        //{...} Your code here, method call etc.
    };
    $scope.verify = function(description, firstname, lastname, dob, address, number, email, linkedin, github) {
        $rootScope.user = JSON.parse(localStorage.getItem("user"));
        $rootScope.user.firstname = firstname;
        $rootScope.user.lastname = lastname;
        $rootScope.user.dob = dob;
        $rootScope.user.address = address;
        $rootScope.user.number = number;
        $rootScope.user.email = email;
        $rootScope.user.linkedin = linkedin;
        $rootScope.user.github = github;
        $rootScope.user.description = description;
        localStorage.setItem("user", JSON.stringify($rootScope.user));
        $location.path('/profile');
    };
});
FinaxysApp.controller('headCtrl', function($scope, $http, $location, $rootScope) {
    $rootScope.session = localStorage.getItem("session");
    $rootScope.type = localStorage.getItem("type");
    $rootScope.user = JSON.parse(localStorage.getItem("user"));
    $scope.deconnexion = function() {
        localStorage.setItem("session", "false");
        localStorage.setItem("type", "");
        $rootScope.session = localStorage.getItem("session");
        $rootScope.type = localStorage.getItem("type");
        $rootScope.experiences = [];
        $rootScope.formation = [];
        $rootScope.skills = [];
        $rootScope.user = {
            "username": "",
            "password": "",
            "firstname": "",
            "lastname": "",
            "dob": "",
            "address": "",
            "number": "",
            "email": "",
            "linkedin": "",
            "github": "",
            "description": "",
            "experiences": $rootScope.experiences,
            "formation": $rootScope.formation,
            "skills": $rootScope.skills
        };
        localStorage.setItem("user", JSON.stringify($rootScope.user));
    };
});
FinaxysApp.controller('ProfileCtrl', function($scope, $http, $location, $rootScope) {
    //localStorage.setItem("picture","css/user.png");
    $rootScope.user = JSON.parse(localStorage.getItem("user"));
    $rootScope.pic = localStorage.getItem("picture");
    //$rootScope.ds=JSON.parse(localStorage.getItem("koala"));
    console.log($rootScope.images[0]);
    if ($rootScope.images != null) {
        $rootScope.pic = $rootScope.images[0];
        localStorage.setItem("picture", $rootScope.pic);
    };
    $scope.edit = function() {
        $location.path('/candidat');
    };
    //localStorage.setItem("picture",$rootScope.pic);
    //if($rootScope.pic == null) $rootScope.pic="css/user.png";
    //$rootScope.image=localStorage.getItem("image");
    //console.log($rootScope.images[0]);
    // var
    //      reader = new FileReader(),
    //      $img = $("#img")[0],
    //      index = $rootScope.images.length;
    //    reader.onload = function (e) {
    //        if ($rootScope.images.indexOf(e.target.result) > -1) return;
    //        $("#imagePreview" + index).attr('src', e.target.result);
    //        $rootScope.uploadImage(index);
    //    }
    //    reader.readAsDataURL($img.files[0]);
});
FinaxysApp.controller('AuthCtrl2', function($scope, $http, $location, $rootScope) {
    if (localStorage.getItem("session") == "true") $location.path('/profileRH');
    // else{
    //     localStorage.setItem("auth", "false");
    //     localStorage.setItem("username", "");
    // }
    $scope.verify = function(username, password) {
        // $http.get("http://"+server+'/login/'+username+'/'+password).
        //         success(function(data) {
        //             $scope.greeting = data;
        //             console.log(data);
        //         });  
        localStorage.setItem("type", "staff");
        localStorage.setItem("session", "true");
        $rootScope.session = localStorage.getItem("session");
        $rootScope.type = localStorage.getItem("type");
        // $rootScope.user=JSON.parse(localStorage.getItem("user"));
        // $rootScope.user.username=username;
        // $rootScope.user.password=password;
        // console.log("controller auth  username= "+$rootScope.user.username);
        // localStorage.setItem("user", JSON.stringify($rootScope.user));
        $location.path('/acceuil');
    }
});