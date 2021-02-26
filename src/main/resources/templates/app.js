function sayHello() {
    var name = document.getElementById('name').value;
    alert("Привет " + name);
    var request = new XMLHttpRequest();
    var url = '/medicine/get?name=' + name;
    request.open("GET", url);
    request.responseType = 'json';
    request.send();
    request.onload = function() {
         var response = JSON.parse(request.response);
         var medicine = response[0][0].name;
         alert(medicine);
    }
}