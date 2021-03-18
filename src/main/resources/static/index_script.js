function submitForm(){
    document.getElementById('defecture').hidden = true;

    let name = document.querySelector('#medicine_name');

    if(name.value.length > 2) {
        cleanTable();
        var table = document.getElementById('table');
        let district = document.querySelector('#district');
        let town = document.querySelector('#town');
        let chain = document.querySelector('#chain');
        let request = new XMLHttpRequest();
        let url = 'http://localhost:8080/medicine/get/all';
        url += '?name=' + name.value;
        url += '&district=' + district.value;
        url += '&town=' + town.value;
        url += '&chain=' + chain.value;
        url += '&pharmacy=' + pharmacy.value;
        url += '&page=' + document.getElementById('pageNumber').value;
        url += '&rows=' + 100;
        request.open("GET", url);
        request.responseType = 'json';
        request.onload = function () {
            var response = request.response;
                validationDate(response);
        }
         request.send();
    }
}

function submitPharmacy(){

    cleanTable();
    let request = new XMLHttpRequest();
    let url = 'http://localhost:8080/medicine/get/all/from/pharmacy';
    url += '&pharmacy=' + pharmacy.value;
    request.open("GET", url);
    request.responseType = 'json';
    request.onload = function () {
        var response = request.response;
            drawTable(response);
    }
     request.send();

}

function validationDate(json) {
     var pageNumber = document.getElementById('pageNumber');
        pageNumber.innerHTML = json['currentPage'];
        if (json['currentPage'] === json['totalPages']) {
            document.getElementById ('next').disabled = true;
        } else {
            document.getElementById ('next').disabled = false;
        }

        if (json['currentPage'] === 1) {
                document.getElementById ('previous').disabled = true;
            } else {
                document.getElementById ('previous').disabled = false;
            }
     if (json['medicines'].length == 0) {
        document.getElementById('defecture').hidden = false;
     }
     drawTable( json['medicines']);
}

function drawTable(data) {

    var name = 'name';
    var price = 'price';
    var quantity = 'quantity';
    var pharmacy = 'pharmacy';
    var address = 'address';
    var telephone_numbers = 'telephoneNumbers';
    var manufacturer = 'manufacturer';
    var id = 'id';
    var district = 'district';
    var date = 'date';

    var table = document.getElementById("table");


    for (var i = 0; i < data.length; i++) {

        tr = table.insertRow(-1);
        var cellName = tr.insertCell(-1);
        cellName.innerHTML = data[i][name];
        var cellPharmacy = tr.insertCell(-1);
        cellPharmacy.innerHTML = data[i][pharmacy][name] + ' (' + data[i][pharmacy][address] + ')';
        var cellPhone = tr.insertCell(-1);
        cellPhone.align = 'center';
        cellPhone.innerHTML = data[i][pharmacy][telephone_numbers];
        var cellDistrict = tr.insertCell(-1);
        cellDistrict.align = 'center';
        if (data[i][pharmacy][district] != null) {
            cellDistrict.innerHTML = data[i][pharmacy][district][name];
        } else  {
            cellDistrict.innerHTML = '-';

        }
        var cellPrice = tr.insertCell(-1);
        cellPrice.align = 'center';
        cellPrice.innerHTML = data[i][price];
        var cellQuantity = tr.insertCell(-1);
        cellQuantity.align = 'center';
        cellQuantity.innerHTML = data[i][quantity];
        var cellManuf = tr.insertCell(-1);
        cellManuf.innerHTML = data[i][manufacturer];
        var cellDate = tr.insertCell(-1);
        cellDate.align = 'center';
        cellDate.innerHTML = data[i][date];
        var cellStat = tr.insertCell(-1);
        cellStat.align = 'center';
        var checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.name = "checkbox";
        let checkboxId = data[i][id];
        checkbox.id = checkboxId ;
        checkbox.onchange = function() {
                                fnselect(checkboxId);
                            }
        cellStat.appendChild (checkbox);
    }
}
function fnselect(id){
    if (document.getElementById(id).checked ) {
        listId.push(id);
        console.log(listId);
    }

    if (!document.getElementById(id).checked ){
        var index = listId.indexOf(id);
        if (index > -1) {
           listId.splice(index, 1);
        }
        console.log(listId);
    }
}

function checkedCheckbox() {
    var checkBoxes = document.getElementsByName('checkbox');
    for(var i=0; i<checkBoxes.length; i++) {
        checkBoxes[i].checked = true;
    }
}


var listId = [];

function statView() {
    if(listId.length > 0) {
        let url = 'http://localhost:8080/statistic/view?medicines=';
        for (var i = 0; i < listId.length - 1; i++) {
            url += listId[i] + ","
        }
        url += listId[listId.length -1];
        let request = new XMLHttpRequest();
        request.open("GET", url);
        request.responseType = 'json';
        request.onload = function () {
            var response = request.response;
            console.log("response");
            clearInput();
            validationDate(response);
            checkedCheckbox();
            document.getElementById('statAdd').hidden = false;

        }
         request.send();



    }
}

function statAdd() {

    if(listId.length > 0) {
        let url = 'http://localhost:8080/statistic/put?medicines=';
            for (var i = 0; i < listId.length - 1; i++) {
                url += listId[i] + ","
            }
            url += listId[listId.length -1];
            url += '&role=' + document.getElementById('role').innerHTML;
            let request = new XMLHttpRequest();
            request.open("GET", url);
            request.responseType = 'json';
            request.onload = function () {
                var response = request.response;
                clearInput();
                cleanTable();
                document.getElementById('statAdd').hidden = true;
                alert('Добавлено записей: ' + response);
                cleanTable();
                listId = [];

            }
             request.send();
    }
}

function clearInput() {
    var medicine_name = document.getElementById('medicine_name');
    medicine_name.value = "";
    var district = document.getElementById('district');
    district.value = "";
    var town = document.getElementById('town');
    town.value = "";
    var chain = document.getElementById('chain');
    chain.value = "";
    var pharmacy = document.getElementById('pharmacy');
    pharmacy.value = "";
    cleanTable();
    document.getElementById('pageNumber').value = 1;
    document.getElementById('previous').disabled = true;
    document.getElementById('next').disabled = true;


}

function cleanTable() {
    for (var i = document.getElementById('table').getElementsByTagName('tr').length -1; i; i--) {
        document.getElementById('table').deleteRow(i);
    }
}


function previousPage() {
    var pageNum = document.getElementById('pageNumber');
    if (pageNumber.value > 1) {
        pageNumber.value--;
        submitForm();
    }

}
function nextPage() {
    var pageNum = document.getElementById('pageNumber');
    pageNumber.value++;
    submitForm();
}

function addDefecture() {
    let url = 'http://localhost:8080/medicine/defecture/put?medicines=';
    url += document.querySelector('#medicine_name').value;
    url += '&role=' + document.getElementById('role').innerHTML;
    let request = new XMLHttpRequest();
    request.open("GET", url);
    request.responseType = 'json';
    request.onload = function () {
        var response = request.response;
        clearInput();
        cleanTable();
        document.getElementById('statAdd').hidden = true;
        document.getElementById('defecture').hidden = true;

        alert('Добавлена запись: ' + response['name'])
    }
     request.send();
}