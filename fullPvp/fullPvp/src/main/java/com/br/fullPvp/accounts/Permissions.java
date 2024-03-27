package com.br.fullPvp.accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permissions {
	
	MANAGE_PROFILE("profile.manager", "Ver e gerenciar informações do perfil"),
	MANAGE_GROUPS("group.manager", "Ver e gerenciar informações dos grupos"),
	CHAT_STAFF("cmd.staff", "Entrar no chat da equipe"),
	ADMIN_MODE("cmd.admin", "Entrar/sair do modo administrador"),
	CHAT_LOCAL("cmd.chatlocal", "Habilitar/desabilitar chat local"),
	CHAT_GLOBAL("cmd.chatglobal", "Habilitar/desabilitar chat global"),
	MANUTENCE("cmd.manutence", "Habilitar/desabilitar manutenção do servidor"),
	GAME_MODE("cmd.gm", "Gerenciar modo de jogo"), 
	REPORTS_SEE("reports.manager", "Ver e gerenciar informações das denúncias"),
	WARNS_SEE("warns.manager", "Ver e gerenciar avisos"),
	MANAGE_RANKS("rank.manager", "Ver e gerenciar informações dos ranks"),
	MANAGE_LINKS("links.manager", "Ver e gerenciar links de midia"),
	MANAGE_WARP("warps.manager", "Ver e gerenciar warps"),
	MANAGE_SHOP("shop.manager", "Ver e gerenciar itens e sessões da loja"),
	MANAGE_KIT("kit.manager", "Ver e gerenciar kits"),
	VANTAGE_WARP("warps.vantage", "Tempo de teleporte reduzido"),
	SPAWN_VIP("spawn.vip", "Acesso ao spawn vip"),
	MANAGE_TAG("tags.manager", "Ver e gerenciar informações das tags"), 
	SAY_CHAT_OFF("chat.vantage", "Usufruir do chat mesmo desabilitado"), 
	CHAT_COLOR("chat.color", "Usufruir de cores no chat"), 
	
	;
	
	String permission, info;

}
