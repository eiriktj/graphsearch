package gs;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class Count
{

    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    @UserFunction(value = "gs.ecount")
    @Description("Count number of expressions connected to work.")
    public String ecount(
            @Name("node") Node node)
    {
        Iterable<Relationship> rels = node.getRelationships(RelTypes.REALIZES);

        int counter = 0;
        for (Object i : rels) {
            counter++;
        }
        return Integer.toString(counter);
    }

    private enum RelTypes implements RelationshipType
    {
        REALIZES
    }
}
