/**
 * Sitmax ExtJS UI
 * Copyright(c) 2011-2012 ICG Inc.
 * @author Johns Castillo Valencia
 */
Ext.ns('domain.UserManager');

domain.errors = {
    mustSelect: function() {
        Ext.MessageBox.show({
            title: 'Aviso',
            msg: 'Debe seleccionar un <b>Registro</b>.',
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.INFO
        });
    },
    mustBeServer: function() {
        Ext.MessageBox.show({
            title: 'Aviso',
            msg: 'Debe seleccionar la <b>Ra&iacute;z del servicio</b>.',
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.INFO
        });
    },
    mustBeOperation: function() {
        Ext.MessageBox.show({
            title: 'Aviso',
            msg: 'Debe seleccionar una <b>Operacion del servicio</b>.',
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.INFO
        });
    },
    submitFailure: function(title, msg) {
        Ext.MessageBox.show({
            title: title,
            msg: msg,
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.ERROR
        });
    },
    operationError: function(title, msg) {
        Ext.MessageBox.show({
            title: title,
            msg: msg,
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.ERROR
        });
    }
};

domain.formButtons = function(options) {
    return [{
            text: 'Guardar',
            iconCls: 'entity-save',
            formBind: true,
            handler: function() {
                options.form.getForm().submit({
                    //waitMsg : 'Guardando...',
                    success: function(form, action) {
                        options.grid.getStore().reload();
                        options.win.close();
                    },
                    failure: function(form, action) {
                        //Ext.Msg.alert('Warning', action.result.errorMessage);
                        //options.error('Error interno',action.result.errorMessage);
                        domain.errors.submitFailure('Error interno', action.result.errorMessage);
                    }
                });
            }
        }, {
            text: 'Deshacer',
            iconCls: 'entity-undo',
            handler: function() {
                options.form.getForm().reset();
            }
        }, '->', {
            text: 'Cerrar',
            handler: function() {
                options.win.close();
            }
        }];
};

domain.ServiceManager = {
    definirServicio: function(options) {
        var form = new Ext.FormPanel({
            url: Ext.SROOT + 'definirservicio',
            border: false,
            autoHeight: true,
            fileUpload: true,
            bodyStyle: 'padding:10px',
            defaults: {
                msgTarget: 'side',
                width: 300
            },
            items: [{
                    xtype: 'textfield',
                    fieldLabel: 'Nombre',
                    allowBlank: false,
                    name: 'nombre'
                }, {
                    xtype: 'textarea',
                    fieldLabel: 'Descripci&oacute;n',
                    allowBlank: false,
                    name: 'descripcion'
                }, {
                    xtype: 'fileuploadfield',
                    emptyText: 'Seleccione una imagen',
                    fieldLabel: 'Imagen',
                    name: 'imagen',
                    buttonText: '',
                    buttonCfg: {
                        iconCls: 'upload-icon'
                    }
                }, {
                    xtype: 'textfield',
                    fieldLabel: 'Xpath',
                    allowBlank: false,
                    value: '/',
                    name: 'responseXpath'
                }, {
                    xtype: "hidden",
                    name: "router",
                    value: options.router,
                }, {
                    xtype: "hidden",
                    name: "url",
                    value: options.url,
                }]
        });

        var win = new Ext.Window({
            title: 'Definir Servicio',
            autoScroll: true,
            autoHeight: true,
            width: 500,
            activeItem: 0,
            layout: 'anchor',
            items: [{
                    xtype: 'panel',
                    bodyStyle: 'padding:10px;background-color:#FFFFFF;color:#777777',
                    html: '<b>Esta acci&oacute;n establece la Operaci&oacute;n Web Service como servicio del sistema.  El Servicio requiere configuraci&oacute;n.</b>',
                    height: 50,
                    border: false
                }, form],
            modal: true,
            buttonAlign: 'center',
            buttons: [{
                    text: 'Guardar',
                    handler: function() {
                        form.getForm().submit({                            
                            success: function(form, action) {
                                options.grid.store.reload();
                                win.close();
                            },
                            failure: function(form, action) {
                                if (action.failureType === 'server') {
                                    var r = Ext.util.JSON.decode(action.response.responseText);
                                    com.icg.errors.submitFailure('Error', r.message);
                                }
                            }
                        });
                    }
                }]
        });
        win.show();
    },
    deleteService: function(options) {
        if (options.node.attributes.iconCls === 'server') {
            var item = options.node.attributes;
            Ext.MessageBox.confirm('Confirmar', '¿Confirma eliminar el registro? Se perderan Datos.', function(r) {
                if (r === 'yes') {
                    Ext.Ajax.request({
                        url: Ext.SROOT + 'eliminarservidor',
                        method: 'POST',
                        params: {
                            id: item.id
                        },
                        success: function(result, request) {
                            options.tree.getRootNode().reload();
                        },
                        failure: function(result, request) {

                        }
                    });
                }
            });
        } else {
            domain.errors.mustBeServer();
        }
    },
    reloadService: function(options) {
        if (options.node.attributes.iconCls === 'server') {
            var item = options.node.attributes;
            Ext.Ajax.request({
                url: Ext.SROOT + 'reloadservidor',
                method: 'POST',
                params: {
                    id: item.id
                },
                success: function(result, request) {
                    options.tree.getRootNode().reload();
                },
                failure: function(result, request) {

                }
            });
        } else {
            domain.errors.mustBeServer();
        }
    },
    openOperation: function(options) {
        options.panelinfo.removeAll();
        if (options.node.attributes.iconCls === 'operation') {
            var item = options.node.attributes;
            Ext.Ajax.request({
                url: Ext.SROOT + 'formserviceitems',
                method: 'POST',
                params: {
                    id: item.id
                },
                success: function(result, request) {
                    var res = Ext.util.JSON.decode(result.responseText);
                    if (res.success) {
                        var sfields = res.data;
                        sfields.push({
                            xtype: 'hidden',
                            name: '__router_swi_var',
                            value: item.id
                        });
                        sfields.push({
                            xtype: 'hidden',
                            name: '__endpoint_swi_var',
                            value: item.url
                        });

                        var ppanel = new Ext.Panel({
                            xtype: 'panel',
                            title: 'Resultado',
                            region: 'center',
                            bodyStyle: 'padding:10px',
                            autoScroll: true,
                            height: 200,
                            html: '<pre  class="brush: xml;"></pre>'
                        });

                        var form = new Ext.FormPanel({
                            url: Ext.SROOT + 'webserviceworld',
                            border: false,
                            autoHeight: true,
                            region: 'north',
                            split: true,
                            bodyStyle: 'padding:10px',
                            labelWidth: 130,
                            frame: true,
                            labelAlign: 'top',
                            defaults: {
                                msgTarget: 'side'
                            },
                            items: sfields,
                            tbar: [{
                                    text: 'Ejecutar',
                                    iconCls: 'play',
                                    waitMsg: 'Procesando...',
                                    tooltip: 'Llamar la operaci&oacute;n del servicio',
                                    handler: function() {
                                        options.panelinfo.getEl().mask("Procesando...", "x-mask-loading");
                                        ppanel.body.update('<pre class="brush: xml;"></pre>');
                                        Ext.Ajax.request({
                                            url: Ext.SROOT + 'webserviceworld',
                                            method: 'POST',
                                            waitMsg: 'Enviando...',
                                            params: form.getForm().getValues(),
                                            success: function(result, request) {
                                                options.panelinfo.getEl().unmask();
                                                var serviceResponse = Ext.util.JSON.decode(result.responseText);
                                                ppanel.body.update('<pre class="brush: xml;">' + serviceResponse.result + '</pre>');

//                                            options.panelinfo.remove(1);
//                                            var ppanel = new Ext.Panel({
//                                                xtype: 'panel',
//                                                title: 'Resultado',
//                                                bodyStyle: 'padding:10px',
//                                                autoScroll: true,
//                                                height: 200,
//                                                html: '<pre  class="brush: xml;">' + serviceResponse.result + '</pre>'
//                                            });
//                                            options.panelinfo.add(ppanel);
                                                //ppanel.body.update('<pre class="brush: xml;">' + result.responseText + '</pre>');
//                                            options.panelinfo.doLayout();
                                                //win.doLayout();
                                            },
                                            failure: function(result, request) {
                                                options.panelinfo.getEl().unmask();
                                                console.log(result);
                                                //var r = Ext.util.JSON.decode(action.response.responseText);
                                                //com.icg.errors.submitFailure('Error', r.message);

                                                Ext.MessageBox.show({
                                                    title: 'Error',
                                                    msg: 'Error del servidor',
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.Msg.ERROR
                                                });
                                            }
                                        });



                                    }
                                }, '-', {
                                    text: 'Definir Servicio',
                                    iconCls: 'accept',
                                    tooltip: 'Definir como Servicio del sistema',
                                    handler: function() {
                                        domain.ServiceManager.definirServicio({
                                            router: item.id,
                                            url: item.url,
                                            grid: options.grid
                                        });
                                    }
                                }]
                        });

                        var panel = new Ext.Panel({
                            layout: 'border',
                            items: [form, ppanel]
                        });
                        options.panelinfo.add(panel);
                        options.panelinfo.doLayout();
                    } else {
                        domain.errors.operationError('Error', 'Error en el Servidor, No implementado');
                    }
                },
                failure: function(result, request) {
                    domain.errors.operationError('Error', 'Error en el Servidor');
                }
            });
        } else {
            domain.errors.mustBeOperation();
        }
    },
    datosServicio: function() {
        return {
            xtype: 'fieldset',
            title: 'Datos de conexion',
            defaults: {
                msgTarget: 'side',
                width: 450
            },
            items: [{
                    xtype: 'textfield',
                    fieldLabel: 'Nombre',
                    allowBlank: false,
                    name: 'nombre'
                }, {
                    xtype: 'textfield',
                    fieldLabel: 'WSDL URL',
                    allowBlank: false,
                    name: 'wsdlurl'
                }]
        };
    },
    newService: function(options) {
        var form = new Ext.FormPanel({
            url: Ext.SROOT + 'crearservicio',
            border: false,
            autoHeight: true,
            bodyStyle: 'padding:10px',
            labelWidth: 100,
            waitMsgTarget: true,
            items: [this.datosServicio()]
        });

        var win = new Ext.Window({
            iconCls: 'server',
            title: 'Registrar Servicio',
            autoScroll: true,
            width: 650,
            autoHeight: true,
            minWidth: 640,
            items: form,
            modal: true,
            buttonAlign: 'center',
            buttons: [{
                    text: 'Guardar',
                    handler: function() {
                        var button = this;
                        button.disabled = true;
                        form.getForm().submit({
                            waitMsg: 'Leyendo WSDL...',
                            success: function(form, action) {
                                options.tree.getRootNode().reload();
                                win.close();
                            },
                            failure: function(form, action) {
                                domain.errors.submitFailure('Error', action.result.message);
                                //console.log(action);
                                button.disabled = false;
                            }
                        });
                    }
                }]
        });
        win.show();
    },
    deleteServiceDef: function(rec, grid) {
        Ext.MessageBox.confirm('Confirmar', '¿Confirma eliminar el registro? Se perderan Datos.', function(r) {
            if (r === 'yes') {
                Ext.Ajax.request({
                    url: Ext.SROOT + 'eliminarservicio',
                    method: 'POST',
                    params: {
                        id: rec.data.id
                    },
                    success: function(result, request) {
                        grid.store.reload();
                    },
                    failure: function(result, request) {

                    }
                });
            }
        });
    },
    editServiceDef: function(options) {
        var form = new Ext.FormPanel({
            url: Ext.SROOT + 'actualizarservicio',
            border: false,
            autoHeight: true,
            fileUpload: true,
            bodyStyle: 'padding:10px',
            defaults: {
                msgTarget: 'side',
                width: 300
            },
            items: [{
                    xtype: 'textfield',
                    fieldLabel: 'Nombre',
                    allowBlank: false,
                    name: 'nombre'
                }, {
                    xtype: 'textarea',
                    fieldLabel: 'Descripci&oacute;n',
                    allowBlank: false,
                    name: 'descripcion'
                }, {
                    xtype: 'fileuploadfield',
                    emptyText: 'Seleccione una imagen',
                    fieldLabel: 'Imagen',
                    name: 'imagen',
                    buttonText: '',
                    buttonCfg: {
                        iconCls: 'upload-icon'
                    }
                }, {
                    xtype: 'textfield',
                    fieldLabel: 'Xpath',
                    allowBlank: false,
                    name: 'responseXpath'
                }, {
                    xtype: "hidden",
                    name: "router"
                }, {
                    xtype: "hidden",
                    name: "url"
                }, {
                    xtype: "hidden",
                    name: "id"
                }]
        });

        form.getForm().loadRecord(options.record);

        var win = new Ext.Window({
            title: 'Definir Servicio',
            autoScroll: true,
            autoHeight: true,
            width: 500,
            activeItem: 0,
            layout: 'anchor',
            items: [form],
            modal: true,
            buttonAlign: 'center',
            buttons: [{
                    text: 'Guardar',
                    handler: function() {
                        form.getForm().submit({
                            success: function(form, action) {
                                options.grid.store.reload();
                                win.close();
                            },
                            failure: function(form, action) {
                                if (action.failureType === 'server') {
                                    var r = Ext.util.JSON.decode(action.response.responseText);
                                    com.icg.errors.submitFailure('Error', r.errorMessage);
                                }
                            }
                        });
                    }
                }]
        });
        win.show();
    }
};

domain.ServiceManager.View = {
    init: function() {

        var tree = new Ext.tree.TreePanel({
            title: 'Servidores',
            iconCls: 'server',
            region: 'north',
            autoScroll: true,
            height: 350,
            split: true,
            collapsible: true,
            animate: true,
            loadMask: true,
            rootVisible: false,
            root: {
                nodeType: 'async'
            },
            loader: new Ext.tree.TreeLoader({
                dataUrl: 'treeservices',
                requestMethod: 'GET'
            }),
            tbar: [{
                    iconCls: 'refresh',
                    tooltip: 'Recargar',
                    handler: function() {
                        tree.getRootNode().reload();
                    }
                }, '-', {
                    text: 'Nuevo',
                    iconCls: 'server-add',
                    tooltip: 'Nuevo servicio',
                    handler: function() {
                        domain.ServiceManager.newService({tree: tree});
                    }
                }, {
                    text: 'Eliminar',
                    iconCls: 'server-delete',
                    tooltip: 'Eliminar servicio',
                    handler: function() {
                        var record = tree.getSelectionModel().getSelectedNode();
                        if (record) {
                            domain.ServiceManager.deleteService({node: record, tree: tree});
                        } else {
                            domain.errors.mustSelect();
                        }
                    }
                }, '-', {
                    iconCls: 'drink',
                    text: 'Expandir',
                    //tooltip: '',
                    handler: function() {
                        this.text = 'ddd',
                                tree.getRootNode().reload();
                        tree.getRootNode().expand(true);
                    }
                }, {
                    iconCls: 'drink',
                    text: 'Contraer',
                    //tooltip: 'Abrir Operaci&oacute;n',
                    handler: function() {
                        //this.text = 'ddd';
                        tree.getRootNode().reload();
                        //tree.getRootNode().expand(true);
                    }
                }, {
                    iconCls: 'page-refresh',
                    text: 'Recargar',
                    handler: function() {
                        var record = tree.getSelectionModel().getSelectedNode();
                        if (record) {
                            domain.ServiceManager.reloadService({node: record, tree: tree});
                        } else {
                            domain.errors.mustSelect();
                        }
                    }
                }, {
                    text: 'Abrir',
                    iconCls: 'open',
                    tooltip: 'Abrir Operaci&oacute;n',
                    handler: function() {
                        var record = tree.getSelectionModel().getSelectedNode();
                        if (record) {
                            domain.ServiceManager.openOperation({
                                node: record,
                                tree: tree,
                                panelinfo: serviceInfoPanel,
                                grid: sgrid
                            });
                        } else {
                            domain.errors.mustSelect();
                        }
                    }
                }],
            listeners: {
                dblclick: function(node, e) {
                    if (node.attributes.iconCls === 'operation') {
                        domain.ServiceManager.openOperation({
                            node: node,
                            tree: tree,
                            panelinfo: serviceInfoPanel,
                            grid: sgrid
                        });
                    }
                }
            }
        });
        //tree.getRootNode().expand(true);
        var serviceInfoPanel = new Ext.Panel({
            title: 'Informacion',
            region: 'center',
            layout: 'fit'
        });


        var sstore = new Ext.data.JsonStore({
            url: Ext.SROOT + 'individual/listaservicios',
            fields: [{name: 'id'},
                {name: 'nombre'}, {name: 'descripcion'}, {name: 'responseXpath'}, {name: 'router'}, {name: 'url'}],
            autoLoad: true
        });

        var cols = [new Ext.grid.RowNumberer({
                width: 27
            }),
            {header: "Nombre", width: 150, autoExpandColumn: true, sortable: true, dataIndex: 'nombre'},
            {header: "Descripci&oacute;n", width: 150, autoExpandColumn: true, sortable: true, dataIndex: 'descripcion'},
            {header: "Xpath", width: 150, autoExpandColumn: true, sortable: true, dataIndex: 'responseXpath'},
            {header: "Servidor", width: 150, autoExpandColumn: true, sortable: true, dataIndex: 'router', renderer: function(val) {
                return val.split(':')[0];
            }}
        ];

        var sgrid = new Ext.grid.GridPanel({
            store: sstore,
            height: 200,
            columns: cols,
            region: 'center',
            //enableDragDrop: true,
            //stripeRows: true,
            loadMask: true,      
            selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
            title: 'Servicios Definidos',
            tbar: [{
                    text: 'Editar',
                    iconCls: 'update',
                    handler: function() {
                        var record = sgrid.getSelectionModel().getSelected();
                        if (record) {
                            domain.ServiceManager.editServiceDef({
                                record: record,
                                grid: sgrid
                            });
                        } else {
                            domain.errors.mustSelect();
                        }
                    }
                }, {
                    text: 'Eliminar',
                    iconCls: 'delete',
                    handler: function() {
                        var record = sgrid.getSelectionModel().getSelected();
                        if (record) {
                            domain.ServiceManager.deleteServiceDef(record, sgrid);
                        } else {
                            domain.errors.mustSelect();
                        }
                    }
                }]
        });


        var izquierda = new Ext.Panel({
            layout: 'border',
            region: 'west',
            collapsible: true,
            split: true,
            width: 450,
            minWidth: 400,
            maxWidth: 550,
            items: [tree, sgrid]
        });



        new Ext.Viewport({
            layout: 'fit',
            border: false,
            items: new Ext.Panel({
                border: false,
                layout: 'border',
                items: [izquierda, serviceInfoPanel]
            })
        });
    }
}

Ext.onReady(domain.ServiceManager.View.init, domain.ServiceManager.View);