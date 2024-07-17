package winthorDb.forms.nfe;

import br.com.sefaz.nfe.schema.v4.TNfeProc;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class NFeXMLImporter {
    
    /**
     *
     * @param caminhoXML
     * @return
     */
    public TNfeProc importarXML(String caminhoXML) {
        TNfeProc obj = null;
        try {
            // 1. Criar uma fábrica de validação
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            // 2. Carregar o schema XSD da NF-e (exemplo de caminho para o XSD)
            Schema schema = schemaFactory.newSchema(new File(getClass().getResource("/winthorDb/forms/nfe/xsd/v400/procNFe_v4.00.xsd").getFile()));
            
            // 3. Criar um contexto JAXB para a classe raiz do XML
            JAXBContext context = JAXBContext.newInstance("br.com.sefaz.nfe.schema.v4");
            
            // 4. Criar um unmarshaller para converter o XML em objetos Java
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            
            // 5. Fazer o unmarshal do XML para objetos Java
            obj = (TNfeProc) unmarshaller.unmarshal(new File(caminhoXML));
            
            // 6. Aqui você pode processar os objetos Java conforme necessário
            // Exemplo: NFe nfe = (NFe) obj;
            
            System.out.println("XML importado e processado com sucesso!");
            
            
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            // Tratamento de erro
        }
        return obj;
    }
}