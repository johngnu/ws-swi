/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.asfi.uif.swi.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 *
 * @author John
 */
@Entity
@Table(name = "servicios")
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "servicio_id")
    private Integer id;
    @Column(unique = true)
    private String nombre;
    private String router;
    private String url;
    @Transient
    private CommonsMultipartFile imagen;
    @Lob
    private String descripcion;
    @Lob
    private String textImage;
    @Lob
    private String requestXmlTemplate;
    @Lob
    private String gridCols;
    private String responseXpath;
    private Boolean rpiEnable = false;
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Parametro> parametros;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getRpiEnable() {
        return rpiEnable;
    }

    public void setRpiEnable(Boolean rpiEnable) {
        this.rpiEnable = rpiEnable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public Collection<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(Collection<Parametro> parametros) {
        this.parametros = parametros;
    }

    public String getRequestXmlTemplate() {
        return requestXmlTemplate;
    }

    public void setRequestXmlTemplate(String requestXmlTemplate) {
        this.requestXmlTemplate = requestXmlTemplate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserService)) {
            return false;
        }
        UserService other = (UserService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.gob.asfi.uif.swi.model.UserService[ id=" + id + " ]";
    }

    public String getResponseXpath() {
        return responseXpath;
    }

    public void setResponseXpath(String responseXpath) {
        this.responseXpath = responseXpath;
    }

    public CommonsMultipartFile getImagen() {
        return imagen;
    }

    public void setImagen(CommonsMultipartFile imagen) {
        this.imagen = imagen;
    }

    public String getTextImage() {
        return textImage;
    }

    public void setTextImage(String textImage) {
        this.textImage = textImage;
    }

    public String getGridCols() {
        return gridCols;
    }

    public void setGridCols(String gridCols) {
        this.gridCols = gridCols;
    }
}
