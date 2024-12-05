import com.groupdocs.editor.EditableDocument;
import com.groupdocs.editor.Editor;
import com.groupdocs.editor.options.SpreadsheetEditOptions;
import com.groupdocs.editor.options.SpreadsheetLoadOptions;

public class ODSWritter {

	public static void Escritor () {
		// Load the ODS file into Editor with the optional SpreadsheetLoadOptions
		Editor editor = new Editor("C:\\\\Users\\\\maytompa\\\\Documents\\\\GitHub\\\\TrabajoGrupalAccesoADatos\\\\RetoGrupal\\\\src\\\\test\\\\resources\\\\example.ods", new SpreadsheetLoadOptions());

		// Create and adjust the edit options
		SpreadsheetEditOptions editOptions = new SpreadsheetEditOptions();
		editOptions.setWorksheetIndex(1);//select a tab (worksheet) to edit
		// Open input ODS document for edit — obtain an intermediate document, that can be edited
		EditableDocument beforeEdit = editor.edit(editOptions);

		// Grab ODS document content and associated resources from editable document
		String content = beforeEdit.getContent();
		System.out.println(content);
	}
}
