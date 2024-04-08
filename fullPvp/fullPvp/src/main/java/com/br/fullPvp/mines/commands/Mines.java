package com.br.fullPvp.mines.commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.br.fullPvp.accounts.Account;
import com.br.fullPvp.accounts.AccountManager;
import com.br.fullPvp.accounts.Permissions;
import com.br.fullPvp.mines.Composition;
import com.br.fullPvp.mines.Mine;
import com.br.fullPvp.mines.MineManager;
import com.br.fullPvp.mines.Position;
import com.br.fullPvp.mines.PositionManager;
import com.br.fullPvp.utils.ItemName;
import com.br.fullPvp.utils.TimeManager;
import com.br.fullPvp.utils.Utils;

public class Mines extends Utils implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) { 
			Player player = (Player) sender;
			Account account = AccountManager.getInstance().get(player.getName());
			if(!account.hasPermission(Permissions.MANAGE_MINE.getPermission())) { 
				sendMessage(player, false, "§cVocê não tem permissão para fazer isso!");
				return true;
			}
			if(args.length == 0) { 
				sintax(sender, label);
				return true;
			} else if(args.length == 1) { 
				if(args[0].equalsIgnoreCase("definirloc")) { 
					Position position = PositionManager.getInstance().getPosition(player.getUniqueId());
					if(position == null) { 
						sendMessage(player, false, "§dVocê entrou no modo marcador!");
						PositionManager.getInstance().add(player.getUniqueId());
						PositionManager.getInstance().getPosition(player.getUniqueId()).give(player);
					} else { 
						sendMessage(player, false, "§cVocê saiu do modo marcador!");
						PositionManager.getInstance().getPosition(player.getUniqueId()).remove(player);
					}
				}
				return true;
			} else if(args.length == 2) { 
				Mine mine = MineManager.getInstance().get(captalize(args[1]));
				if(args[0].equalsIgnoreCase("criar")) { 
					Position position = PositionManager.getInstance().getPosition(player.getUniqueId());
					if(position != null && position.getPos1() != null && position.getPos2() != null) { 
						if(mine == null) { 
							MineManager.getInstance().add(new Mine(captalize(args[1]), "5m", 0L, false, false, PositionManager.getInstance().getPosition(player.getUniqueId()).getPos1(), PositionManager.getInstance().getPosition(player.getUniqueId()).getPos2(), null, new ArrayList<Block>(), new ArrayList<Composition>()));
							sendMessage(sender, false, "§aMina §7" + captalize(args[1]) + " §acriada!");
							position.remove(player);
							PositionManager.getInstance().remove(player.getUniqueId());
						} else { 
							sendMessage(sender, false, "§cEsta mina já existe!");
						}
					} else { 
						sendMessage(player, false, "§cVocê precisa demarcar uma localização!");
					}
				} else if(args[0].equalsIgnoreCase("deletar")) { 
					if(mine != null) { 
						sendMessage(sender, false, "§aMina §7" + mine.getName() + " §adeletada!");
						mine.delete();
						MineManager.getInstance().remove(mine);
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				} else if(args[0].equalsIgnoreCase("info")) { 
					if(mine != null) { 
						StringBuilder list = new StringBuilder();
						for(Composition c : mine.getComposition()) { 
							list.append(ItemName.valueOf(Material.getMaterial(c.getId()), c.getDurability()).getName() + "§8(" + formatPercent(c.getPercent()) + ")§7, ");
						}
						sendMessage(sender, true, 
								  "§aMina §7" + mine.getName() + "§a:", 
								  "§aLocalização mina: ",
								  "  §7X-1: " + mine.getPos1().getBlockX() + ", Y-1:" + mine.getPos1().getBlockY(),
								  "  §7Z-1: " + mine.getPos2().getBlockX() + ", Y-2:" + mine.getPos2().getBlockY(),
								  "  §7Mundo: " + mine.getPos1().getWorld().getName(),
								  "§aTempo reset §7" + compareTime(TimeManager.getInstance().getTime(mine.getTimeToReset())),
								  "§aAtivada? §7" + (mine.isEnable() ? "Sim" : "Não"),
								  "§aHolograma? §7" + (mine.isEnableHolo() ? "Sim" : "Não"),
								  "§aComposição: §7" + (list.length() <= 0 ? "§cNenhum bloco adicionado!" : list.toString().substring(0, list.length() - 2)));
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				} else if(args[0].equalsIgnoreCase("resetar")) { 
					if(mine != null) { 
						if(!mine.getComposition().isEmpty()) { 
							mine.reset();
							sendMessage(player, false, "§aMina §7" +  mine.getName() + " §aresetada manualmente!");
						} else { 
							sendMessage(player, false, "§cEsta mina não tem composição!");
						}
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				}
				return true;
			} else if(args.length == 3) { 
				if(args[0].equalsIgnoreCase("comp")) { 
					Mine mine = MineManager.getInstance().get(captalize(args[2]));
					if(mine != null) { 
						if(player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) { 
							if(args[1].equalsIgnoreCase("remover")) { 
								if(mine.containsBlock(player.getItemInHand().getTypeId(), player.getItemInHand().getDurability())) { 
									mine.remove(player.getItemInHand());
									sendMessage(player, false, "§aRemovido §7" + ItemName.valueOf(player.getItemInHand().getType(), player.getItemInHand().getDurability()).getName() + " §ada mina §7" + mine.getName() + "§a!");
								} else { 
									sendMessage(player, false, "§cEste bloco não faz parte desta mina!");
								}
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa estar com um item válido na mão!");
						}
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				} else if(args[0].equalsIgnoreCase("definirconf")) { 
					Mine mine = MineManager.getInstance().get(captalize(args[1]));
					if(mine != null) { 
						if(args[0].equalsIgnoreCase("func")) { 
							if(mine.isComplete()) {
								mine.setEnable(mine.isEnable() ? false : true);
								mine.setTime(TimeManager.getInstance().getTime(mine.getTimeToReset()));
								mine.reset();
								sendMessage(sender, false, "§aMina §7" + mine.getName() + " §aagora " + (mine.isEnable() ? "está" : "não está") + " ativa!");
							} else { 
								sendMessage(player, false, "§cA mina deve estar com a composição completa!");
							}
						}
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				}
				return true;
			} else if(args.length == 4) { 
				if(args[0].equalsIgnoreCase("comp")) { 
					Mine mine = MineManager.getInstance().get(captalize(args[2]));
					if(mine != null) { 
						if(player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) { 
							if(args[1].equalsIgnoreCase("add")) { 
								if(!mine.containsBlock(player.getItemInHand().getTypeId(), player.getItemInHand().getDurability())) { 
									if(mine.remaing() >= Double.valueOf(args[3])) { 
										if(isDouble(args[3])) { 
											mine.add(player.getItemInHand(), Integer.valueOf(args[3]));
											sendMessage(player, false, "§aAdicionado §f" + formatPercent(Double.valueOf(args[3])) + " §ade §7" + ItemName.valueOf(player.getItemInHand().getType(), player.getItemInHand().getDurability()).getName() + " §aà mina §7" + mine.getName() + "§a!");
										} else { 
											sendMessage(sender, false, "§cNúmero inválido!");
										}
									} else { 
										sendMessage(player, false, "§cFaltam §f" + formatPercent(mine.remaing()) + " §cpara completar esta composição!");
									}
								} else { 
									sendMessage(player, false, "§cParte da composição já é de " + ItemName.valueOf(player.getItemInHand().getType(), player.getItemInHand().getDurability()).getName() + "!");
								}
							}
						} else { 
							sendMessage(player, false, "§cVocê precisa estar com um item válido na mão!");
						}
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				} else if(args[0].equalsIgnoreCase("definirconf")) { 
					Mine mine = MineManager.getInstance().get(captalize(args[2]));
					if(mine != null) { 
						if(args[1].equalsIgnoreCase("tempo")) {
							if(isTime(args[3])) { 
								mine.setTimeToReset(args[3]);
								mine.setTime(TimeManager.getInstance().getTime(args[3]));
								sendMessage(player, false, "§aTempo para resetar mina §7" + mine.getName() + " §aalterado para §f" + TimeManager.getInstance().getTime(args[3]) + "§a!");
							} else { 
								sendMessage(sender, false, "§cTempo inválido!");
								return true;
							}
						}
					} else { 
						sendMessage(sender, false, "§cEsta mina não existe!");
					}
				}
				return true;
			}
		} else { 
			sendMessage(sender, false, "Somente jogadores podem digitar este comando!");
		}
		return false;
	}
	
	private void sintax(CommandSender sender, String label) { 
		sintaxCommand(sender, "§c/" + label + " definirloc", 
							  "§c/" + label + " <criar, deletar, info, resetar> <nome>",
							  "§c/" + label + " definirconf <nome> holo",
							  "§c/" + label + " definirconf <nome> locholo",
							  "§c/" + label + " definirconf <nome> func",
							  "§c/" + label + " definirconf <nome> tempo <tempo para resetar>",
							  "§c/" + label + " comp <add> <nome> <porcentagem>",
							  "§c/" + label + " comp <remover> <nome>");
	}
}
