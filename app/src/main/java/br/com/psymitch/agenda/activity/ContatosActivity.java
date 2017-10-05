package br.com.psymitch.agenda.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import br.com.psymitch.agenda.fragment.ListaContatosFragment;
import br.com.psymitch.agenda.R;
import br.com.psymitch.agenda.fragment.VisualizarFormularioFragment;
import br.com.psymitch.agenda.modelo.Contato;

public class ContatosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.frame_principal,new ListaContatosFragment());

        if (estaNoModoPaisagem()){
            tx.replace(R.id.frame_secundario,new VisualizarFormularioFragment());
        }

        tx.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    private boolean estaNoModoPaisagem() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionaContatos(Contato contato) {

        FragmentManager maneger = getSupportFragmentManager();

        if (!estaNoModoPaisagem()){

            FragmentTransaction tx = maneger.beginTransaction();
            VisualizarFormularioFragment visualizarFragment = new VisualizarFormularioFragment();
            Bundle parametros = new Bundle();
            parametros.putSerializable("contato",contato);
            visualizarFragment.setArguments(parametros);

            tx.replace(R.id.frame_principal,visualizarFragment);
            tx.addToBackStack(null);
            tx.commit();

        }else{

            VisualizarFormularioFragment visualizarFragment = (VisualizarFormularioFragment) maneger.findFragmentById(R.id.frame_secundario);
            visualizarFragment.populaCampo(contato);

        }
    }
}
