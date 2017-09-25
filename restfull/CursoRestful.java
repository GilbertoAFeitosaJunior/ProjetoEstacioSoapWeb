package mobi.stos.projetoestacio.restfull;


import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mobi.stos.projetoestacio.bean.Curso;
import mobi.stos.projetoestacio.bo.ICursoBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author feito
 */
@Component
@Path("curso")
public class CursoRestful {

    @Autowired
    private ICursoBo iCursoBo;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listar")
    public Response list() {
        try {
            List<Curso> list = this.iCursoBo.listall();
            return Response.status(Response.Status.OK).entity(list).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/salvar")
    public Response salvar(Curso curso) {
        try {
            this.iCursoBo.persist(curso);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/consultar/{id}")
    public Response consultar(@PathParam("id") long id) {
        try {
            Curso curso = this.iCursoBo.load(id);
            if(curso==null){
               return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.OK).entity(curso).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    
     @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/editar")
    public Response editar(Curso curso) {
       try {
            Curso cursoRetrno = this.iCursoBo.load(curso.getId());
            if(cursoRetrno==null){
               return Response.status(Response.Status.NOT_FOUND).build();
            }else{
                this.iCursoBo.persist(curso);
            }
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
