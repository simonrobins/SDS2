var versionsList = {
	"11.200" : {
		"AIX" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"HP" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"IBM" : {
			"DB2" : [ "CP285" ]
		},
		"RED" : {
			"NON" : [ "NON" ]
		},
		"SOL" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"WIN" : {
			"ORA" : [ "ASC", "UNI" ],
			"MSQ" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		}
	},
	"11.300" : {
		"AIX" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"HI" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"HP" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"IBM" : {
			"DB2" : [ "CP285", "CP37" ]
		},
		"RED" : {
			"NON" : [ "NON" ]
		},
		"SOL" : {
			"ORA" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		},
		"WIN" : {
			"ORA" : [ "ASC", "UNI" ],
			"MSQ" : [ "ASC", "UNI" ],
			"SYB" : [ "ASC" ]
		}
	},
	"12.000" : {
		"AIX" : {
			"ORA" : [ "ASC", "UNI" ]
		},
		"HI" : {
			"ORA" : [ "ASC", "UNI" ]
		},
		"IBM" : {
			"DB2" : [ "CP285", "CP37", "UNI" ]
		},
		"RED" : {
			"NON" : [ "NON" ]
		},
		"SOL" : {
			"ORA" : [ "ASC", "UNI" ]
		},
		"WIN" : {
			"ORA" : [ "ASC", "UNI" ],
			"MSQ" : [ "ASC", "UNI" ]
		}
	}
}

function choosePlatform() {
	var selectedPlatform = document.getElementById("selectedPlatform").value.replace(/^\s+|\s+$/g, "");
	var version = document.getElementById("version").value;
	var platformList = versionsList[version];

	var select = document.getElementById("platform");
	select.options.length = 0;
	for(var key in platformList)
	{
		var platformName = platforms[key];
		var selected = key == selectedPlatform;
		var option = new Option(platformName, key);
		option.selected = selected; 
		select.options[select.options.length] = option; 
	}

	chooseDB();
}

function chooseDB() {
	var selectedDatabase = document.getElementById("selectedDatabase").value.replace(/^\s+|\s+$/g, "");
	var version = document.getElementById("version").value;
	var platform = document.getElementById("platform").value;
	var databaseList = versionsList[version][platform];

	var fin = document.getElementById("fin");
	fin.disabled = platform == "RED";

	var select = document.getElementById("database");
	select.options.length = 0;
	for(var key in databaseList)
	{
		var databaseName = databases[key];
		var selected = key == selectedDatabase;
		var option = new Option(databaseName, key);
		option.selected = selected;
		select.options[select.options.length] = option; 
	}

	chooseEnc();
}

function chooseEnc() {
	var selectedEncoding = document.getElementById("selectedEncoding").value.replace(/^\s+|\s+$/g, "");
	var version = document.getElementById("version").value;
	var platform = document.getElementById("platform").value;
	var database = document.getElementById("database").value;
	var encodingList = versionsList[version][platform][database];

	var select = document.getElementById("encoding");
	select.options.length = 0;
	for(var index in encodingList)
	{
		var key = encodingList[index];

		var encodingName = encodings[key];
		var selected = key == selectedEncoding;
		var option = new Option(encodingName, key);
		option.selected = selected;
		select.options[select.options.length] = option;
	}
}

function doSelect(onOrOff)
{
	var productList = document.getElementById("products");
	var products = productList.getElementsByTagName("input");
	
	for(var index in products)
	{
		var element = products[index];
		if(element.type == "checkbox")
			element.checked = onOrOff;
	}
}

window.onload = function() { choosePlatform() };
