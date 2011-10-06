function init(){
    var infovis = document.getElementById('graph_void');
    var w = infovis.offsetWidth - 50, h = infovis.offsetHeight - 50;
    
    //init Hypertree
    var ht = new $jit.Hypertree({
      //id of the visualization container
      injectInto: 'graph_void',
      //canvas width and height
      width: w,
      height: h,
      //Change node and edge styles such as
      //color, width and dimensions.
      Node: {
          dim: 9,
          overridable: true,
          color: "#f00"
      },
      Edge: {
          lineWidth: 2,
          overridable: true,
          color: "#088"
      },
      onBeforeCompute: function(node){
      },
      //Attach event handlers and add text to the
      //labels. This method is only triggered on label
      //creation
      onCreateLabel: function(domElement, node){
          domElement.innerHTML = node.name;
          $jit.util.addEvent(domElement, 'click', function () {
              ht.onClick(node.id, {
                  onComplete: function() {
                      ht.controller.onComplete();
                  }
              });
          });
      },
    });
    //load JSON data.
    ht.loadJSON(void_tree);
    //compute positions and plot.
    ht.refresh();
    //end
    ht.controller.onComplete();
}

