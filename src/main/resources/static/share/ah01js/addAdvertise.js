var txtarea = document.getElementById("description")
		, spnText = document.getElementById("spnText")
		, titleBox = document.getElementById("titleBox")
		, spnTitle = document.getElementById("spnTitle");
txtarea.onkeyup = txtareaSetLen;
titleBox.onkeyup = titleSetLen;
function txtareaSetLen(){
	spnText.innerHTML = 'Description: ' + (512 - txtarea.value.length) + ' chars left';
}
function titleSetLen(){
	spnTitle.innerHTML = 'Title: ' + (64 - titleBox.value.length) + ' chars left';
}
function addCategoriesSection(pid, ptext, depth){
	//delete all
	var allCats = document.getElementById("allCategories").children;
	for (var i = 0; i < allCats.length; i++) {
		if (allCats[i].getAttribute("depth") > depth) {
			allCats[i].remove();
			i--;
		}
	}
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState == 4 && this.status == 200){
			console.log(this.responseText);
			let obj = JSON.parse(this.responseText);
			console.log(this.responseText);
			if (obj.length>0){
				var childStr = '<section depth="' + (depth+1).toString() + '" id="cats_' + pid +'" class="panel" style="margin-bottom: 5px;"><header class="panel-heading">Category:: ' + ptext + '</header><div  id="allCategories" class="tagsinput ah01-margin5_inner_childs" style="height: 100%;">';
				obj.forEach(function(item){
					childStr += '<div id="cat_' + item.id +'" class="btn-group"><a onclick="clickTag(' + item.id + ',' + (depth+1).toString() + ')" class="btn btn-primary" href="javascript:void(0)">' + (item.text != null?item.text:'+undefined+') + '</a>' + (pid > 0 ?((advCats.some(item2 => item2.id === item.id)) ?'<a class="btn btn-danger" onclick="removeCat(' + item.id + ')" href="javascript:void(0)"><i class="icon_close_alt2"></i></a>':'<a class="btn btn-success" onclick="addCat(' + item.id + ')" href="javascript:void(0)"><i class="icon_check_alt2"></i></a>'):'') + '</div>';
				});
				childStr += '</div></section>';
				document.getElementById("allCategories").insertAdjacentHTML('beforeend', childStr);
			}
		}
	};
	xhttp.open("GET", basedomain+"api/advCategories?parentId="+pid+"&lan="+currentLocale, true);
	xhttp.send();
	//add list
}
window.onload = function () {
	addCategoriesSection(0, "Root", 0);
	txtareaSetLen();
	titleSetLen();
};
function publishAdv(){
	if (isSending) return;
	isSending = true;
	console.log("----------------------------------------------");
	var xhr  = new XMLHttpRequest();
	var data = new FormData(), advContent={};
	advContent.categories = advCats;
	advContent.title = document.getElementById('titleBox').value;
	advContent.text = document.getElementById('description').value;
	advContent.webSiteLink = document.getElementById('advLink').value;

	data.append("pic1",document.getElementById('pic1').files[0]);
	data.append("advContent",JSON.stringify(advContent));

	xhr.onload = function() {
		if (this.status == 200){
			document.getElementById('clickSuccess').click();
			resetForm();
		} else document.getElementById('clickFail').click();
		isSending = false;
	}
	xhr.open("POST", basedomain+"api/advertises", true);
	xhr.send(data);
}
function resetForm(){
	advCats = [];
	document.getElementById('advForm').reset();
	document.getElementById('advCatsDiv').innerHTML = "";
	addCategoriesSection(0, "Root", 0);
}

var isSending = false, advCats = [];
function clickTag(id, depth){
	var editBtn = document.querySelector("a[href='#myModal-1']")
			,editForm =  document.getElementById('myModal-1');
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState == 4 && this.status == 200){
			const cat = JSON.parse(this.responseText);
			console.log(currentLang);
			addCategoriesSection(id, cat.category[currentLang], depth);
		}
	};
	xhttp.open("GET", basedomain + "api/advCategory?id=" + id, true);
	xhttp.send();
}
function addCat(id){
	var cat = {}
	cat.id = id;
	if (!advCats.some(item => item.id === id) && advCats.length<5){
		var selCat = document.getElementById('cat_' + id);
		advCats.push(JSON.parse(JSON.stringify(cat)));

		var catText = selCat.querySelector('.btn-primary').text;
		var childStr = '<div id="advCat_' + id + '" class="btn-group"><a style="float: left;background-color: olive;border: 1px solid;border-color: olive;border-radius: 4px 0px 0px 4px;padding: 6px 12px;text-align: center;color: aliceblue;">' + catText + '</a><a class="btn btn-danger" onclick="removeCat(' + id + ')" href="javascript:void(0)"><i class="icon_close_alt2"></i></a></div>';
		document.getElementById("advCatsDiv").insertAdjacentHTML('beforeend', childStr);

		selCat.querySelector('.btn-success').remove();
		selCat.insertAdjacentHTML('beforeend', '<a class="btn btn-danger" onclick="removeCat(' + id + ')" href="javascript:void(0)"><i class="icon_close_alt2"></i></a>');
	}
}
function removeCat(id){
	for (var i = 0; i < advCats.length; i++) {
		if (advCats[i].id === id){
			document.getElementById('advCat_' + id).remove();
			var selCat = document.getElementById('cat_' + id);
			if (selCat != null){
				selCat.querySelector('.btn-danger').remove();
				selCat.insertAdjacentHTML('beforeend', '<a class="btn btn-success" onclick="addCat(' + id + ')" href="javascript:void(0)"><i class="icon_check_alt2"></i></a>');
			}
			advCats.splice(i, 1);
			break;
		}
	}
}
