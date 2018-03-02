package org.openrefine.wikidata.testing;

import java.util.Collections;

import org.openrefine.wikidata.schema.WbLanguageConstant;
import org.openrefine.wikidata.schema.WbMonolingualExpr;
import org.openrefine.wikidata.schema.WbStringConstant;
import org.openrefine.wikidata.schema.entityvalues.ReconItemIdValue;
import org.openrefine.wikidata.schema.entityvalues.ReconPropertyIdValue;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;

import com.google.refine.model.Cell;
import com.google.refine.model.Recon;
import com.google.refine.model.ReconCandidate;

public class TestingDataGenerator {
    
    protected static PropertyIdValue pid = Datamodel.makeWikidataPropertyIdValue("P38");
    
    public static Recon makeNewItemRecon(long judgementId) {
        Recon recon = Recon.makeWikidataRecon(judgementId);
        recon.judgment = Recon.Judgment.New;
        return recon;
    }
    
    public static Recon makeMatchedRecon(String qid, String name, String[] types) {
        Recon recon = Recon.makeWikidataRecon(123456L);
        recon.match = new ReconCandidate(qid, name, types, 100.0);
        recon.candidates = Collections.singletonList(recon.match);
        recon.judgment = Recon.Judgment.Matched;
        return recon;
    }
    
    public static Recon makeMatchedRecon(String qid, String name) {
        return makeMatchedRecon(qid, name, new String[0]);
    }
    
    public static Cell makeNewItemCell(long judgementId, String name) {
        return new Cell(name, makeNewItemRecon(judgementId));
    }
    
    public static Cell makeMatchedCell(String qid, String name) {
        return new Cell(name, makeMatchedRecon(qid, name));
    }
    
    public static ReconItemIdValue makeNewItemIdValue(long judgementId, String name) {
        return new ReconItemIdValue(makeNewItemRecon(judgementId), name);
    }
    
    public static ReconItemIdValue makeMatchedItemIdValue(String qid, String name) {
        return new ReconItemIdValue(makeMatchedRecon(qid, name), name);
    }

    public static ReconPropertyIdValue makeNewPropertyIdValue(long judgmentId, String name) {
        return new ReconPropertyIdValue(makeNewItemRecon(judgmentId), name);
    }
    
    public static ReconPropertyIdValue makeMatchedPropertyIdValue(String pid, String name) {
        return new ReconPropertyIdValue(makeMatchedRecon(pid, name), name);
    }
    
    public static WbMonolingualExpr getTestMonolingualExpr(String langCode, String langLabel, String text) {
        return new WbMonolingualExpr(new WbLanguageConstant(langCode, langLabel), new WbStringConstant(text));
    }
    
    public static Statement generateStatement(ItemIdValue from, PropertyIdValue pid, ItemIdValue to) {
        Claim claim = Datamodel.makeClaim(from, Datamodel.makeValueSnak(pid, to), Collections.emptyList());
        return Datamodel.makeStatement(claim, Collections.emptyList(), StatementRank.NORMAL, "");
    }
    
    public static Statement generateStatement(ItemIdValue from, ItemIdValue to) {
        return generateStatement(from, pid, to);
    }

    public static ItemIdValue newIdA = makeNewItemIdValue(1234L, "new item A");
    public static ItemIdValue newIdB = makeNewItemIdValue(4567L, "new item B");
    public static ItemIdValue matchedId = makeMatchedItemIdValue("Q89","eist");
    public static ItemIdValue existingId = Datamodel.makeWikidataItemIdValue("Q43");
    
    
}
