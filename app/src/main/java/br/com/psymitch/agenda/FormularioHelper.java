package br.com.psymitch.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.psymitch.agenda.activity.FormularioActivity;
import br.com.psymitch.agenda.modelo.Contato;

/**
 * Created by CsGo on 09/03/2017.
 */

public class FormularioHelper {
    private final EditText campoNome;
    private final EditText campoTelefone;
    private final EditText campoEndereco;
    private final EditText campoEmail;
    private final RatingBar campoNota;
    private final ImageView campoFoto;
    private final EditText campoSite;
    private Contato contato;

    //Construtor
    public FormularioHelper(FormularioActivity activity) {
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoEmail = (EditText) activity.findViewById(R.id.formulario_email);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        campoFoto = (ImageView) activity.findViewById(R.id.formulario_foto);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        contato = new Contato();
    }

    //Carregando os objetos de tela com oa valores do objeto contato
    public Contato preencheFormulario(Contato contato) {
        campoNome.setText(contato.getNome());
        campoTelefone.setText(contato.getTelefone());
        campoEndereco.setText(contato.getEndereco());
        campoEmail.setText(contato.getEmail());
        campoSite.setText(contato.getSite());
        campoNota.setProgress(contato.getNota().intValue());
        carregaImagem(contato.getCaminhoFoto());
        this.contato = contato;
        return contato;
    }

    //Carregando o objeto com as informações que foram preenchidos na tela
    public Contato pegaContato() {
        contato.setNome(campoNome.getText().toString());
        contato.setTelefone(campoTelefone.getText().toString());
        contato.setEndereco(campoEndereco.getText().toString());
        contato.setEmail(campoEmail.getText().toString());
        contato.setSite(campoSite.getText().toString());
        contato.setNota(Double.valueOf(campoNota.getProgress()));
        contato.setCaminhoFoto((String) campoFoto.getTag());
        return contato;
    }

    //Carrega a foto tirada no objeto da tela e no objeto
    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 1240, 1080, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.CENTER);
            campoFoto.setTag(caminhoFoto);
            contato.setCaminhoFoto(caminhoFoto);
        }

    }
}
