package br.com.psymitch.agenda.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import br.com.psymitch.agenda.FormularioHelper;
import br.com.psymitch.agenda.R;
import br.com.psymitch.agenda.modelo.Contato;


public class VisualizarFormularioFragment extends Fragment {

    private FormularioHelper helper;
    private TextView campoNome;
    private TextView campoEndereco;
    private TextView campoSite;
    private TextView campoTelefone;
    private ImageView campoFoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visualizar_formulario, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        campoNome = (TextView) view.findViewById(R.id.formulario_nome);
        campoEndereco = (TextView) view.findViewById(R.id.formulario_endereco);
        campoSite = (TextView) view.findViewById(R.id.formulario_site);
        campoTelefone = (TextView) view.findViewById(R.id.formulario_telefone);
        campoFoto = (ImageView) view.findViewById(R.id.formulario_foto);

        Bundle parametros = getArguments();

        if (parametros != null){
            Contato contato = (Contato) parametros.getSerializable("contato");
            populaCampo(contato);
            //helper.preencheFormulario(contato);
        }

        //setHasOptionsMenu(true);
        return view;
    }

    public void populaCampo (Contato contato){
        campoNome.setText(contato.getNome());
        campoEndereco.setText(contato.getEndereco());
        campoSite.setText(contato.getSite());
        campoTelefone.setText(contato.getTelefone());
        carregaImagem(contato.getCaminhoFoto(),contato);

    }

    public void carregaImagem(String caminhoFoto,Contato contato) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 1240, 1080, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.CENTER);
            campoFoto.setTag(caminhoFoto);
            contato.setCaminhoFoto(caminhoFoto);
        }else{
            campoFoto.setImageResource(R.drawable.person);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            contato.setCaminhoFoto(caminhoFoto);
        }
    }

}
