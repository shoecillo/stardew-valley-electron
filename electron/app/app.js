



stardewApp.service("queries", ["$http","$q",function($http,$q) {

	var vm = this;
	vm.getAll = function()
	{
		return $http.get('data.json').then(function(result)
		{
			return result.data;
		},
		function(result)
		{
			return $q.reject(result.data);
		});
	};

}]);

stardewApp.controller("stardewCtrl",["$scope","$cookies","queries",function($scope,$cookies,queries)
{
	var vm = $scope;
	vm.lsAll = [];
	vm.lsInmutable = [];
	vm.selected = "";
	vm.tamaField = "";
	vm.report = "";
	vm.repVisible = false;
	vm.carrito = [];
	vm.totales = "";
	vm.titulo = "";
	vm.available = "";
	vm.regex = "\\d+"
	vm.panelGrf = false;
	vm.calGrow = [];
		
	vm.dynamic = 20;
    vm.type = "warning";
    vm.showWarning = true;
	
	/*
  if($cookies.get("ShoeCookie") == null)
	{
		location.href = "http://boot2docker.dev.com:8000/login.html";
  }
	*/
	queries.getAll().then(function(data)
	{
		vm.lsAll = data;
		vm.lsInmutable = data;
	},function(error)
	{
		  location.href = "http://boot2docker.dev.com:8000/login.html";
	});

	vm.calcular = function()
	{
		vm.repVisible = false;
		vm.report = {};
		var totalDias = 28;
		var grow = parseInt(vm.selected.growing) +1;
		

		var res = 28 / grow;
		var multip = 1;
		vm.report.name = vm.selected.name;
		vm.report.img = vm.selected.img;
		vm.report.fields = vm.tamaField;
		if(vm.selected.multip != null)
		{
			multip = parseInt(vm.selected.multip);
		}
		if(vm.selected.produce == null)
		{
			vm.report.cantidad = Math.floor(res) * vm.tamaField;
			vm.report.precio = vm.report.cantidad * parseInt(vm.selected.buyPierre);
			vm.report.cosechas = Math.floor(res);
			vm.report.ventaBasica = (vm.report.cantidad * multip) * parseInt(vm.selected.saleRegular);
		}
		else
		{
			vm.report.cantidad = vm.tamaField;
			vm.report.precio = vm.report.cantidad * parseInt(vm.selected.buyPierre);
			vm.report.cosechas = Math.floor((28 - grow)/parseInt(vm.selected.produce));
			vm.report.ventaBasica = vm.report.cosechas * (parseInt(vm.selected.saleRegular) * (vm.tamaField * multip));
		}
		
	};
	
	var fnCalendar = function()
	{
		var totalDias = 28;
		var grow = parseInt(vm.selected.growing) +1;
		vm.calGrow = [];
		if(vm.selected.produce == null)
		{
			var conta = 1;
			for(var i=1;i<=totalDias;i++)
			{
				var day = {};
				day.dia = i;
				day.cosecha = false;
				if(grow == conta)
				{
					conta = 1;
					day.cosecha = true;
				}
				else
				{
					conta++
				}
				vm.calGrow.push(day);
			}
		}
		else
		{
			var conta = 1;
			var isGrown = false;
			for(var i=1;i<=totalDias;i++)
			{
				var day = {};
				day.dia = i;
				day.cosecha = false;
				if(grow == conta && !isGrown)
				{
					conta = 1;
					day.cosecha = true;
					isGrown = true;
				}
				else if(isGrown && parseInt(vm.selected.produce) == conta)
				{
					conta = 1;
					day.cosecha = true;
					isGrown = true;
				}
				else
				{
					conta++
				}
				vm.calGrow.push(day);
			}
		}
	};

	vm.onSelect = function($item, $model, $label)
	{
		vm.report = "";
		vm.tamaField = "";
		vm.repVisible = false;
		vm.titulo = "Detalles de Semilla";
		fnCalendar();
	};

	vm.addCarrito = function()
	{
		var flagAdd = true;
		if(vm.carrito.length > 0)
		{
			for(var i=0;i<vm.carrito.length;i++)
			{
				if(vm.carrito[i].name == vm.selected.name)
				{
					flagAdd = false;
					break;
				}
			}
		}
		if(flagAdd)
		{
			vm.carrito.push(vm.report);
			vm.totales = {};
			fnTotales();
			vm.dynamic = Math.round((vm.totales.huecos * 100)/vm.available);
		}
	};

	vm.reporteTotal = function()
	{
		vm.report = "";
		vm.tamaField = "";
		vm.repVisible = true;
		vm.totales = {};
		vm.titulo = "Detalles del Carrito";
		fnTotales();


	};

	var fnTotales = function()
	{
		vm.totales.totalGasto = 0;
		vm.totales.totalBeneficio = 0;
		vm.totales.totalRentabilidad = 0;
		vm.totales.huecos = 0;
		for(var i=0;i<vm.carrito.length;i++)
		{
			vm.totales.totalGasto += vm.carrito[i].precio;
			vm.totales.totalBeneficio += vm.carrito[i].ventaBasica;
			vm.totales.totalRentabilidad += vm.carrito[i].ventaBasica - vm.carrito[i].precio;
			vm.totales.huecos += vm.carrito[i].fields;
		}
	};

	vm.selectedItem = function(crop)
	{
		vm.selected = crop;
		fnCalendar();
	};

	vm.remCarrito = function(item)
	{

		for(var i=0;i < vm.carrito.length;i++)
		{
			if(item.name == vm.carrito[i].name)
			{
				vm.carrito.splice(i, 1);
				fnTotales();
				vm.dynamic = Math.round((vm.totales.huecos * 100)/vm.available);
				if(vm.carrito.length == 0)
				{
					vm.repVisible = false;
					vm.titulo = "Detalles de Semilla";
				}
				break;
			}
		}
	};

	vm.add24 = function()
	{
		if(vm.tamaField == "")
		{
			vm.tamaField = 24;
		}
		else
		{
			vm.tamaField += 24;
		}
	};

	vm.rem24 = function()
	{
		if(vm.tamaField == "" || vm.tamaField == 0)
		{
			vm.tamaField = 0;
		}
		else
		{
			vm.tamaField -= 24;
		}
	};

	vm.filterSeason = function(season)
	{
		vm.lsAll = [];
		if(season == "all")
		{
			vm.lsAll = vm.lsInmutable;
		}
		else
		{
			for(var i=0;i<vm.lsInmutable.length;i++)
			{
				var item = vm.lsInmutable[i].season;
				var ls = item.split(",");
				ls = ls.filter(function(it)
				{
					return (season == it);
				});
				if(ls.length > 0)
				{
					vm.lsAll.push(vm.lsInmutable[i]);
				}
			}
		}
	};
	
	vm.filtrarNum = function(evt)
	{
		
		if(vm.available != "")
		{
			if(evt.keyCode < 48 || evt.keyCode > 57)
			{
				if(evt.keyCode !=8)
				{
					var index = vm.available.length -1;
					if(index > -1)
					{
						var start = vm.available.substring(0,index);
						vm.available = start;
					}
				}	
			}
		}
	};
	
	vm.grafica = function()
	{
		vm.totales = {};
		fnTotales();
		
		vm.dynamic = Math.round((vm.totales.huecos * 100)/vm.available);
		vm.type = "success";
		vm.showWarning = true;
		vm.panelGrf = true;
	
	};

}]);

stardewApp.component("goldImg",{
    template: "<img src='img/Gold.png' width='16' height='16'>"
});

stardewApp.component("bichoImg",{
    template: "<img src='img/Junimo_Icon.png' width='32' height='32'>"
});
