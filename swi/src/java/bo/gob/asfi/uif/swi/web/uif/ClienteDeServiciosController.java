/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.asfi.uif.swi.web.uif;

import bo.gob.asfi.uif.swi.dao.Dao;
import bo.gob.asfi.uif.swi.model.ElementParam;
import bo.gob.asfi.uif.swi.model.FormField;
import bo.gob.asfi.uif.swi.model.Node;
import bo.gob.asfi.uif.swi.model.ORequest;
import bo.gob.asfi.uif.swi.model.Operacion;
import bo.gob.asfi.uif.swi.model.Parametro;
import bo.gob.asfi.uif.swi.model.Puerto;
import bo.gob.asfi.uif.swi.model.Servicio;
import bo.gob.asfi.uif.swi.model.Servidor;
import bo.gob.asfi.uif.swi.model.UserService;
import com.google.gson.Gson;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.Service;
import com.predic8.wsdl.WSDLParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author John
 */
@Controller
public class ClienteDeServiciosController {

    @Autowired
    Dao dao;

    @RequestMapping(value = "/administrarservicios")
    public String administrarServicios() {
        return "servicios/administrarServicios";
    }

    @RequestMapping(value = "/servicios")
    public @ResponseBody
    List<Servidor> servicios() {

        List<Servidor> servicios = dao.findAll(Servidor.class);
        return servicios;
    }

    @RequestMapping(value = "/crearservicio", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ? extends Object> crearServicio(Servidor servidor) {
        Map<String, Object> body = new HashMap<String, Object>();
        try {
            WSDLParser parser = new WSDLParser();
            Definitions defs = parser.parse(servidor.getWsdlurl());
            servidor.setJsonstruct(this.jsonStruct(defs));
            dao.persist(servidor);
            body.put("success", true);
            return body;
        } catch (DataIntegrityViolationException e) {
            body.put("message", "Nombre del Servicio ya existe");
        } catch (Exception e) {
            body.put("message", "WSDL no valido");
        }
        body.put("success", false);
        return body;
    }

    private String jsonStruct(Definitions definitions) {
        List<Servicio> servicios = new ArrayList<Servicio>();
        for (Service srv : definitions.getServices()) {
            Servicio s = new Servicio();
            s.setNombre(srv.getName());
            s.setPuertos(new ArrayList<Puerto>());
            for (Port port : srv.getPorts()) {
                System.out.println("p   " + port.getName());
                Puerto p = new Puerto();
                p.setNombre(port.getName());
                p.setDireccion(port.getAddress().getLocation());
                p.setOperaciones(new ArrayList<Operacion>());
                for (BindingOperation op : port.getBinding().getOperations()) {
                    System.out.println("    op " + op.getName());
                    Operacion o = new Operacion();
                    o.setNombre(op.getName());
                    o.setBindingName(port.getBinding().getName());
                    //System.out.println("  def ::" + op.getBinding().getPortType().getName());
                    Operation operation = definitions.getOperation(op.getName(), op.getBinding().getPortType().getName());
                    if (!operation.getInput().getMessage().getParts().isEmpty()) {
                        //System.out.println(" size " + );
                        Element elreq = operation.getInput().getMessage().getParts().get(0).getElement();
                        if (elreq != null) {
                            o.setRequest(new ORequest());
                            o.getRequest().setElements(this.listParameters(elreq));

                            //Element elres = operation.getOutput().getMessage().getParts().get(0).getElement();
                            //System.out.println("Elem params   " + elres); 
                            //o.setResponse(new OResponse());
                            //o.getResponse().setElements(this.listParameters(elres));
                        }
                    }

                    p.getOperaciones().add(o);
                }
                s.getPuertos().add(p);
            }
            servicios.add(s);
        }
        return new Gson().toJson(servicios);
    }

    @RequestMapping(value = "/eliminarservidor", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ? extends Object> eliminarServidor(@RequestParam Long id) {
        Map<String, Object> body = new HashMap<String, Object>();

        Servidor s = dao.get(Servidor.class, id);
        dao.remove(s);

        body.put("success", true);
        return body;
    }

    @RequestMapping(value = "/reloadservidor", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ? extends Object> reloadServidor(@RequestParam Long id) {
        Map<String, Object> body = new HashMap<String, Object>();
        try {
            Servidor servidor = dao.get(Servidor.class, id);
            WSDLParser parser = new WSDLParser();
            Definitions defs = parser.parse(servidor.getWsdlurl());
            servidor.setJsonstruct(this.jsonStruct(defs));
            dao.update(servidor);
            body.put("success", true);
        } catch (Exception e) {
        }
        body.put("success", false);
        return body;
    }

    private List<org.heyma.core.extjs.components.Node> parseJsonStructServiceTree(String rootId, List<Servicio> servicios) {
        List<org.heyma.core.extjs.components.Node> nodes = new ArrayList<org.heyma.core.extjs.components.Node>();

        for (Servicio srv : servicios) {
            Node ns = new Node();
            ns.setText(srv.getNombre());
            ns.setIconCls("service");
            ns.setChildren(new ArrayList<org.heyma.core.extjs.components.Node>());
            for (Puerto prt : srv.getPuertos()) {
                Node nb = new Node();
                nb.setText(prt.getNombre());
                nb.setIconCls("binding");
                nb.setChildren(new ArrayList<org.heyma.core.extjs.components.Node>());
                for (Operacion op : prt.getOperaciones()) {
                    Node no = new Node();
                    no.setText(op.getNombre());
                    no.setId(rootId + ":" + srv.getNombre() + ":" + prt.getNombre() + ":" + op.getNombre() + ":" + op.getBindingName());
                    no.setUrl(prt.getDireccion());
                    no.setIconCls("operation");
                    no.setLeaf(Boolean.TRUE);
                    nb.getChildren().add(no);
                }
                ns.getChildren().add(nb);
            }
            nodes.add(ns);
        }
        return nodes;
    }

    private List<Node> nodesFromServidores(List<Servidor> servidores) {
        List<Node> nodes = new ArrayList<Node>();

        for (Servidor s : servidores) {
            Node n = new Node();
            n.setText(s.getNombre());
            n.setIconCls("server");
            n.setId(s.getId().toString());
            n.setChildren(this.parseJsonStructServiceTree(s.getId().toString(), s.getServicios()));
            nodes.add(n);
        }
        return nodes;
    }

    private List<Node> nodesFromUserServices(List<UserService> services) {
        List<Node> nodes = new ArrayList<Node>();

        for (UserService us : services) {
            if (us.getRpiEnable()) {
                Node n = new Node();
                n.setText(us.getNombre());
                n.setIconCls("service");
                n.setId(us.getId().toString());
                //n.setChildren(this.parseJsonStructServiceTree(s.getId().toString(), s.getServicios()));
                List<org.heyma.core.extjs.components.Node> pms = new ArrayList<org.heyma.core.extjs.components.Node>();
                for (Parametro pm : us.getParametros()) {
                    // para verificar q el parametro no haya sido asignado
                    if (pm.getRpifield() == null) {
                        Node np = new Node();
                        np.setText(pm.getEtiqueta());
                        np.setIconCls("param");
                        np.setChecked(Boolean.FALSE);
                        np.setId(pm.getId().toString());
                        np.setLeaf(Boolean.TRUE);
                        pms.add(np);
                    }
                }
                n.setChildren(pms);
                nodes.add(n);
            }
        }
        return nodes;
    }

    @RequestMapping(value = "/treeservices")
    public @ResponseBody
    List<Node> treeServices() {
        List<Servidor> servidores = dao.findAll(Servidor.class);
        List<Node> nodes = this.nodesFromServidores(servidores);
        return nodes;
    }

    @RequestMapping(value = "/treeuserservices")
    public @ResponseBody
    List<Node> treeUserServices() {
        List<UserService> services = dao.findAll(UserService.class);
        List<Node> nodes = this.nodesFromUserServices(services);
        return nodes;
    }

    private List<ElementParam> listParameters(Element element) {

        ArrayList<ElementParam> listElement = new ArrayList<ElementParam>();
        ComplexType ct = (ComplexType) element.getEmbeddedType();

        if (ct == null) {
            ct = element.getSchema().getComplexType(element.getType().getLocalPart());
        }

        if (ct.getSequence() != null) {
            for (Element e : ct.getSequence().getElements()) {
                ElementParam ne = new ElementParam();
                ne.setName(e.getName());
                System.out.println("====== e " + e);
                ne.setType(e.getType().getLocalPart());
                listElement.add(ne);
            }
        }
        return listElement;
    }

    @RequestMapping(value = "/formserviceitems")
    public @ResponseBody
    Map<String, ? extends Object> formServiceItems(@RequestParam(value = "id") String opId) {
        Map<String, Object> body = new HashMap<String, Object>();
        //parts = ["Root-Rervidor_id","ServiceName","PortName","OperationName"]
        String[] parts = opId.split(":");
        Servidor servidor = dao.get(Servidor.class, new Long(parts[0]));
        List<Servicio> servicios = servidor.getServicios();

        Operacion op = ClienteDeServiciosController.getOperacion(parts[1], parts[2], parts[3], servicios);
        //System.out.println(op);
        if (op != null) {
            if (op.getRequest() != null) {
                body.put("success", true);
                body.put("data", requestFormFiends(op.getRequest()));
                return body;
            }
        }
        body.put("success", false);
        return body;
    }

    private List<FormField> requestFormFiends(ORequest request) {
        List<FormField> list = new ArrayList<FormField>();
        for (ElementParam ep : request.getElements()) {
            FormField ff = new FormField();
            ff.setFieldLabel(ep.getName() + "  (" + ep.getType() + ")");
            ff.setXtype(FormField.TEXT_FIELD);
            ff.setName(ep.getName());
            list.add(ff);
        }
        return list;
    }

    public static Operacion getOperacion(String serviceName, String portName, String operationName, List<Servicio> servicios) {
        for (Servicio s : servicios) {
            if (s.getNombre().equals(serviceName)) {
                for (Puerto p : s.getPuertos()) {
                    if (p.getNombre().equals(portName)) {
                        for (Operacion o : p.getOperaciones()) {
                            if (o.getNombre().equals(operationName)) {
                                return o;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @RequestMapping(value = "/definirservicio", method = RequestMethod.POST)
    public @ResponseBody
    String crearServicio(UserService serv) {
        try {
            if (serv.getImagen() != null && serv.getImagen().getSize() != 0) {
                byte[] bytes = IOUtils.toByteArray(serv.getImagen().getInputStream());
                //String imagen = new sun.misc.BASE64Encoder().encode(bytes);                                                
                Base64 encode = new Base64();
                String imagen = encode.encodeAsString(bytes);
                serv.setTextImage(imagen);
            }
            dao.persist(serv);
            String[] parts = serv.getRouter().split(":");
            Servidor servidor = dao.get(Servidor.class, new Long(parts[0]));
            List<Servicio> servicios = servidor.getServicios();

            Operacion op = ClienteDeServiciosController.getOperacion(parts[1], parts[2], parts[3], servicios);
            if (op != null) {
                if (op.getRequest() != null) {
                    for (ElementParam ep : op.getRequest().getElements()) {
                        Parametro param = new Parametro();
                        param.setNombre(ep.getName());
                        param.setEtiqueta(ep.getName());
                        param.setTipo(ep.getType());
                        param.setServicio(serv);
                        dao.persist(param);
                    }
                }
            }
            return "{success:true}";
        } catch (Exception e) {
        }
        return "{success:false}";
    }

    @RequestMapping("/photo/{id}")
    public ResponseEntity<byte[]> testphoto(@PathVariable Integer id) throws IOException {
        //InputStream in = servletContext.getResourceAsStream("/images/no_image.jpg");
        UserService us = dao.get(UserService.class, id);
        if (us.getTextImage() != null) {
            Base64 decoder = new Base64();
            byte[] imgBytes = decoder.decode(us.getTextImage());

            InputStream stream = new ByteArrayInputStream(imgBytes);

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<byte[]>(IOUtils.toByteArray(stream), headers, HttpStatus.CREATED);
        } else {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("bo/gob/asfi/uif/swi/model/desktop.gif");
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_GIF);
            return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/eliminarservicio", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ? extends Object> eliminarServicio(@RequestParam Integer id) {
        Map<String, Object> body = new HashMap<String, Object>();
        try {
            UserService s = dao.get(UserService.class, id);
            dao.remove(s);
            body.put("success", true);
            return body;
        } catch (DataIntegrityViolationException e) {
            body.put("message", "El servicio está asignado a usuarios..");
        } catch (Exception e) {
            body.put("message", "Error del servidor");
        }
        body.put("success", false);
        return body;
    }

    @RequestMapping(value = "/actualizarservicio", method = RequestMethod.POST)
    public @ResponseBody
    String actualizarServicio(UserService serv) {
        try {
            UserService s = dao.get(UserService.class, serv.getId());
            s.setNombre(serv.getNombre());
            s.setDescripcion(serv.getDescripcion());
            s.setResponseXpath(serv.getResponseXpath());

            if (serv.getImagen() != null && serv.getImagen().getSize() != 0) {
                byte[] bytes = IOUtils.toByteArray(serv.getImagen().getInputStream());
                //String imagen = new sun.misc.BASE64Encoder().encode(bytes);                                                
                Base64 encode = new Base64();
                String imagen = encode.encodeAsString(bytes);
                s.setTextImage(imagen);
            }
            dao.update(s);
            return "{success:true}";
        } catch (Exception e) {
        }
        return "{success:false}";
    }

    @RequestMapping(value = "/servidor/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, ? extends Object> servidor(@PathVariable Long id) {
        Map<String, Object> body = new HashMap<String, Object>();

        Servidor s = dao.get(Servidor.class, id);

        body.put("servidor", s);
        body.put("success", true);
        return body;
    }
}
