package br.gov.jfrj.siga.sr.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ATRIBUTO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrAtributo extends HistoricoSuporte {

	public static ActiveRecord<SrAtributo> AR = new ActiveRecord<>(SrAtributo.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ATRIBUTO_SEQ", name = "srAtributoSeq")
	@GeneratedValue(generator = "srAtributoSeq")
	@Column(name = "ID_ATRIBUTO")
	private Long idAtributo;

	@Column(name = "NOME")
	private String nomeAtributo;

	@Column(name = "DESCRICAO")
	private String descrAtributo;

	@Column(name = "TIPO_ATRIBUTO")
	@Enumerated
	private SrTipoAtributo tipoAtributo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_OBJETIVO")
	private SrObjetivoAtributo objetivoAtributo;

	@Column(name = "DESCR_PRE_DEFINIDO")
	private String descrPreDefinido;

	@Column(name = "CODIGO_ATRIBUTO")
	private String codigoAtributo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrAtributo atributoInicial;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "atributoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	private List<SrAtributo> meuAtributoHistoricoSet;

	@Override
	public Long getId() {
		return idAtributo;
	}

	@Override
	public void setId(Long id) {
		idAtributo = id;
	}

	public Long getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(Long idAtributo) {
		this.idAtributo = idAtributo;
	}

	public String getNomeAtributo() {
		return nomeAtributo;
	}

	public void setNomeAtributo(String nomeAtributo) {
		this.nomeAtributo = nomeAtributo;
	}

	public String getDescrAtributo() {
		return descrAtributo;
	}

	public void setDescrAtributo(String descrAtributo) {
		this.descrAtributo = descrAtributo;
	}

	public SrTipoAtributo getTipoAtributo() {
		return tipoAtributo;
	}

	public void setTipoAtributo(SrTipoAtributo tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}

	public SrObjetivoAtributo getObjetivoAtributo() {
		return objetivoAtributo;
	}

	public void setObjetivoAtributo(SrObjetivoAtributo objetivoAtributo) {
		this.objetivoAtributo = objetivoAtributo;
	}

	public String getDescrPreDefinido() {
		return descrPreDefinido;
	}

	public void setDescrPreDefinido(String descrPreDefinido) {
		this.descrPreDefinido = descrPreDefinido;
	}

	public String getCodigoAtributo() {
		return codigoAtributo;
	}

	public void setCodigoAtributo(String codigoAtributo) {
		this.codigoAtributo = codigoAtributo;
	}

	public SrAtributo getAtributoInicial() {
		return atributoInicial;
	}

	public void setAtributoInicial(SrAtributo atributoInicial) {
		this.atributoInicial = atributoInicial;
	}

	public List<SrAtributo> getMeuAtributoHistoricoSet() {
		return meuAtributoHistoricoSet;
	}

	public void setMeuAtributoHistoricoSet(List<SrAtributo> meuAtributoHistoricoSet) {
		this.meuAtributoHistoricoSet = meuAtributoHistoricoSet;
	}

	public static List<SrAtributo> listarParaSolicitacao(
			boolean mostrarDesativados) throws Exception{
		SrObjetivoAtributo obj = SrObjetivoAtributo
				.AR.findById(SrObjetivoAtributo.OBJETIVO_SOLICITACAO);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listarParaAcordo(boolean mostrarDesativados) throws Exception {
		SrObjetivoAtributo obj = SrObjetivoAtributo
				.AR.findById(SrObjetivoAtributo.OBJETIVO_ACORDO);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listarParaIndicador(
			boolean mostrarDesativados) throws Exception {
		SrObjetivoAtributo obj = SrObjetivoAtributo
				.AR.findById(SrObjetivoAtributo.OBJETIVO_INDICADOR);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listar(SrObjetivoAtributo objetivo,
			boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();

		if (!mostrarDesativados) {
			queryBuilder.append(" hisDtFim is null");
		} else {
			queryBuilder.append("SELECT ta FROM SrAtributo ta ");
			queryBuilder
					.append("WHERE ta.idAtributo in (SELECT MAX(idAtributo) FROM SrAtributo GROUP BY hisIdIni) ");
		}

		if (objetivo != null)
			queryBuilder.append(" and objetivoAtributo.idObjetivo = "
					+ objetivo.getIdObjetivo());

		return SrAtributo.AR.find(queryBuilder.toString()).fetch();
	}

	public List<SrAtributo> getHistoricoAtributo() {
		if (atributoInicial != null)
			return atributoInicial.meuAtributoHistoricoSet;
		return null;
	}

	public SrAtributo getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrAtributo> sols = getHistoricoAtributo();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public Set<String> getPreDefinidoSet() {
		Set<String> preDefinidos = new HashSet<String>();
		if (tipoAtributo == SrTipoAtributo.VL_PRE_DEFINIDO) {
			preDefinidos.addAll(Arrays.asList(descrPreDefinido.split(";")));
		}
		return preDefinidos;
	}

	public List<SrConfiguracao> getAssociacoes(DpLotacao lotaTitular,
			DpPessoa pess) {
		try {
			SrConfiguracao confFiltro = new SrConfiguracao();
			confFiltro.setLotacao(lotaTitular);
			confFiltro.setDpPessoa(pess);
			confFiltro
					.setCpTipoConfiguracao(
							em()
							.find(CpTipoConfiguracao.class,
									CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
			return SrConfiguracao.listar(confFiltro,
					new int[] { SrConfiguracaoBL.TIPO_ATRIBUTO });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SrAtributo get(String codigo) {
		return SrAtributo.AR.find("byCodigoAtributo", codigo).first();
	}

	@Override
	public void salvarComHistorico() throws Exception {

		if (objetivoAtributo == null)
			throw new IllegalStateException("Objetivo nao informado");

		super.salvarComHistorico();
	}

	public String asGetter() {
		if (codigoAtributo == null || codigoAtributo.isEmpty())
			return null;
		return "get" + codigoAtributo.substring(0, 1).toUpperCase()
				+ codigoAtributo.substring(1);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.idAtributo.equals(((SrAtributo)obj).idAtributo);
	}
}