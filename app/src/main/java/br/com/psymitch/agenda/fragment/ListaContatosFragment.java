package br.com.psymitch.agenda.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.psymitch.agenda.R;
import br.com.psymitch.agenda.activity.ContatosActivity;
import br.com.psymitch.agenda.activity.FormularioActivity;
import br.com.psymitch.agenda.adapter.ContatoAdapter;
import br.com.psymitch.agenda.dao.ContatoDAO;
import br.com.psymitch.agenda.modelo.Contato;

public class ListaContatosFragment extends Fragment {

    public static final int CODIGO_LIGACAO = 123;
    private ListView listaContato;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_contatos, container, false);

        listaContato = (ListView) view.findViewById(R.id.lista_contato);

        //Trata clique curto no item da lista
        listaContato.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                Contato contato = (Contato) listaContato.getItemAtPosition(position);
                Toast.makeText(getContext(), contato.getNome() + " Selecionado", Toast.LENGTH_SHORT).show();
                ContatosActivity contatosActivity = (ContatosActivity) getActivity();
                contatosActivity.selecionaContatos(contato);
            }
        });

        //Botao para adicionar novo contato
        Button novoContato = (Button) view.findViewById(R.id.novo_contato);
        novoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaiPraVisualizarFormulario = new Intent(getContext(), FormularioActivity.class);
                startActivity(intentVaiPraVisualizarFormulario);
            }

        });

        //Registrando o menu de contexto(menu que abre quando clica em um nome da lista
        registerForContextMenu(listaContato);

        return view;
    }

    @Override
    public void onResume() {
        carregaLista();
        super.onResume();
    }

    //Metodo que pega do banco os contatos da agenda
    private void carregaLista() {
        ContatoDAO dao = new ContatoDAO(getContext());
        List<Contato> contatos = dao.buscaContatos();
        dao.close();
        ContatoAdapter adapter = new ContatoAdapter(getContext(), contatos);
        listaContato.setAdapter(adapter);
    }

    //Especificando o Menu de Contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contato = (Contato) listaContato.getItemAtPosition(info.position);

        //Ligar
        final MenuItem itemLigar = menu.add("Ligar para " + contato.getNome());
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, 123);
                }

                Intent intentLigar = new Intent(Intent.ACTION_CALL);
                intentLigar.setData(Uri.parse("tel:" + contato.getTelefone()));

                startActivity(intentLigar);

                return false;
            }
        });

        //SMS
        MenuItem itemSMS = menu.add("Enviar SMS para " + contato.getNome());
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + contato.getTelefone()));
        itemSMS.setIntent(intentSMS);

        //Mapa
        MenuItem intemMapa = menu.add("Localização no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + contato.getEndereco()));
        intemMapa.setIntent(intentMapa);

        //Site
        MenuItem itemSite = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

        String site = contato.getSite();

        if (site != null) {
            if (!site.startsWith("https://")) {
                site = "https://" + site;
            }
        }

        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);

        //Editar
        MenuItem itemEditar = menu.add("Editar Contato");
        itemEditar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intentVaiProFormulario = new Intent(getContext(),FormularioActivity.class);
                intentVaiProFormulario.putExtra("contato",contato);
                startActivity(intentVaiProFormulario);
                return false;
            }
        });

        //Deletar
        MenuItem deletar = menu.add("Deletar Contato");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ContatoDAO dao = new ContatoDAO(getContext());
                dao.deletar(contato);
                dao.close();
                carregaLista();
                Toast.makeText(getContext(), "Contato " + contato.getNome() + " Deletado", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

}
