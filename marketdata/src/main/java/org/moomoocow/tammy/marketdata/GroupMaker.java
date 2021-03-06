package org.moomoocow.tammy.marketdata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.model.Exchange;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockGroup;
import org.moomoocow.tammy.model.util.Helper;

public class GroupMaker {

  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(GroupMaker.class);
  
  static String[] etfs = {
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY TECHNOLOGY S&P US SEL SECTOR SOURCE ETF","XLKS",
	  
	  "ProShares UltraPro QQQ ETF","TQQQ",
	  "ProShares UltraPro Russell2000 ETF","URTY",
	  "Direxion Daily Small Cp Bull 3X Shs(ETF)","TNA",
	  "PowerShares Dynamic Networking (ETF)","PXQ",
	  "Direxion Daily Financial Bull 3X Shares","FAS",
	  "ProShares UltraPro S&P 500 (ETF)","UPRO",
	  "SPDR Wells Fargo Prfd Stk ETF","PSK",
	  "ProShares Ultra QQQ (ETF)","QLD",
	  "ProShares UltraShort MidCap400 (ETF)","MZZ",
	  "Direxion Large Cap Bull 3X Shares (ETF)","SPXL",
	  "iShares NASDAQ Biotechnology Index (ETF)","IBB",
	  "PowerShares Preferred Portfolio(ETF)","PGX",
	  "ProShares UltraPro Dow30 ETF","UDOW",
	  "ProShares Ultra Oil & Gas (ETF)","DIG",
	  "iShares S&P US Pref Stock Idx Fnd (ETF)","PFF",
	  "Direxion Daily Energy Bull 3X Shs(ETF)","ERX",
	  "PowerShares Fin. Preferred Port. (ETF)","PGF",
	  "JPMorgan Chase Capital XVI JP Morgan Alerian MLP ETN","AMJ",
	  "ProShares Ultra Russell2000 (ETF)","UWM",
	  "ProShares Ultra Real Estate (ETF)","URE",
	  "SPDR S&P 600 Small Cap Value ETF","SLYV",
	  "ProShares Ultra S&P500 (ETF)","SSO",
	  "ProShares Ultra Financials (ETF)","UYG",
	  "Direxion Daily Semiconductor Bear 3X Shs","SOXS",
	  "First Trust ISE Revere Natural Gas (ETF)","FCG",
	  "Vanguard Utilities ETF","VPU",
	  "PowerShares Dynamic Media Portfol. (ETF)","PBS",
	  "Utilities SPDR (ETF)","XLU",
	  "iShares Dow Jones US Telecom (ETF)","IYZ",
	  "PowerShares Dynamic Biotech &Genome(ETF)","PBE",
	  "SPDR Series Trust","XTL",
	  "ProShares Ultra Health Care (ETF)","RXL",
	  "SPDR KBW Regional Banking (ETF)","KRE",
	  "Rydex S&P Equal Weight Health Care (ETF)","RYH",
	  "ProShares UltraPro MidCap400 ETF","UMDD",
	  "SPDR KBW Bank (ETF)","KBE",
	  "First Trust DJ Internet Index Fund (ETF)","FDN",
	  "Direxion Daily Tech Bull 3x Shs (ETF)","TECL",
	  "ProShares Ultra Russell2000 Growth (ETF)","UKK",
	  "SPDR S&P Biotech (ETF)","XBI",
	  "PowerShares QQQ Trust, Series 1 (ETF)","QQQ",
	  "ProShares Ultra Nasdaq Biotechnology ETF","BIB",
	  "iShares Dow Jones US Reg Banks Ind.(ETF)","IAT",
	  "PowerShares Dynamic OTC Portfolio (ETF)","PWO",
	  "iShares Dow Jones US Aerospace & Def.ETF","ITA",
	  "WisdomTree SmallCap Earnings Fund (ETF)","EES",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY FINANCIALS S&P US SEL SECTOR SOURCE ETF","XLFS",
	  "ProShares Ultra Dow30 (ETF)","DDM",
	  "Rydex S&P MidCap 400 Pure Growth ETF","RFG",
	  "First Trust Utilities AlphaDEX Fnd (ETF)","FXU",
	  "PowerShares Nasdaq Internet Portfol(ETF)","PNQI",
	  "Direxion Daily Mid Cap Bear 3X Shs(ETF)","MIDZ",
	  "Consumer Discretionary SPDR (ETF)","XLY",
	  "Fidelity NASDAQ Comp. Index Trk Stk(ETF)","ONEQ",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY CONS DISC S&P US SEL SECTOR SOURCE ETF","XLYS",
	  "Vanguard Growth ETF","VUG",
	  "First Trust ISE Water Index Fund (ETF)","FIW",
	  "Market Vectors Steel (ETF)","SLX",
	  "SPDR S&P 600 Small Cap ETF","SLY",
	  "Rydex S&P SmallCap 600 Pure Value ETF","RZV",
	  "iShares Russell 2000 Value Index (ETF)","IWN",
	  "First Trust Cnsmer Disry Alpha Fnd (ETF)","FXD",
	  "iShares Russell 2000 Index (ETF)","IWM",
	  "ProShares Ultra MidCap400 (ETF)","MVV",
	  "PowerShares FTSE RAFI US 1500 Small-Mid","PRFZ",
	  "iShares Dow Jones US Financial (ETF)","IYF",
	  "Financial Select Sector SPDR (ETF)","XLF",
	  "PowerShares DWA Technical Ldrs Pf (ETF)","PDP",
	  "iShares S&P SmallCap 600 Value Idx (ETF)","IJS",
	  "iShares Russell 1000 Growth Index (ETF)","IWF",
	  "Vanguard Financials ETF","VFH",
	  "AdvisorShares Trust","HDGE",
	  "iShares S&P SmallCap 600 Index (ETF)","IJR",
	  "Schwab U S Small Cap ETF","SCHA",
	  "PowerShares WilderHill Clean Energy(ETF)","PBW",
	  "iShares S&P 500 Growth Index (ETF)","IVW",
	  "First Trust NASDAQ-100 Ex-Tech Sec (ETF)","QQXT",
	  "iShares Dow Jones US Brok-Dea. Ind.ETF","IAI",
	  "Rydex ETF Trust","EWRS",
	  "WisdomTree Earnings 500 Fund (ETF)","EPS",
	  "Rydex S&P Equal Weight ETF","RSP",
	  "Claymore Beacon Spin-Off (ETF)","CSD",
	  "Direxion Daily Mid Cap Bull 3X Shs(ETF)","MIDU",
	  "iShares Dow Jones US Real Estate (ETF)","IYR",
	  "ProShares Ultra Technology (ETF)","ROM",
	  "Vanguard Small-Cap Growth ETF","VBK",
	  "iShares S&P MidCap 400 Value Index (ETF)","IJJ",
	  "iShares Morningstar Small Value (ETF)","JKL",
	  "iShares Dow Jones US Industrial (ETF)","IYJ",
	  "iShares S&P 500 Index (ETF)","IVV",
	  "iShares Core S&P Mid Cap ETF","IJH",
	  "SPDR Dow Jones Industrial Average ETF","DIA",
	  "Vanguard Scottsdale Funds","VTWO",
	  "iShares Dow Jones Select Dividend (ETF)","DVY",
	  "SPDR S&P Oil & Gas Explore & Prod. (ETF)","XOP",
	  "iShares Russell 1000 Index (ETF)","IWB",
	  "iShares Russell Midcap Value Index (ETF)","IWS",
	  "Vanguard Total Stock Market ETF","VTI",
	  "First Trust S&P REIT Index Fund (ETF)","FRI",
	  "iShares Dow Jones US Utilities (ETF)","IDU",
	  "Vanguard Mid-Cap Value ETF","VOE",
	  "SPDR S&P 500 ETF Trust","SPY",
	  "Vanguard Large-Cap ETF","VV",
	  "Vanguard Index Funds","VB",
	  "iShares S&P SmallCap 600 Growth (ETF)","IJT",
	  "iShares S&P 100 Index (ETF)","OEF",
	  "iShares Dow Jones US Financial Svc.(ETF)","IYG",
	  "Claymore/Sabrient Stealth ETF","WMCR",
	  "SPDR S&P Dividend (ETF)","SDY",
	  "Vanguard Extended Market ETF","VXF",
	  "SPDR S&P Retail (ETF)","XRT",
	  "iShares Russell Midcap Index Fund (ETF)","IWR",
	  "Vanguard Information Technology ETF","VGT",
	  "iShares Dow Jones US Energy Sector (ETF)","IYE",
	  "Rydex Russell Top 50 ETF","XLG",
	  "iShares Dow Jones US Healthcare (ETF)","IYH",
	  "Sector Spdr Trust Sbi","XLI",
	  "SPDR KBW Capital Markets (ETF)","KCE",
	  "PowerShares Dynamic Lg. Cap Value (ETF)","PWV",
	  "iShares Russell 2000 Growth Index (ETF)","IWO",
	  "PowerShares Dividend Achievers (ETF)","PFM",
	  "iShares Russell 3000 Value Index (ETF)","IWW",
	  "Vanguard Consumer Discretionary ETF","VCR",
	  "Vanguard 500 Index Fund","VOO",
	  "ProShares UltraShort SmallCap600 (ETF)","SDD",
	  "Health Care SPDR (ETF)","XLV",
	  "Rydex S&P Equal Weight Technology (ETF)","RYT",
	  "WisdomTree MidCap Dividend Fund (ETF)","DON",
	  "Vanguard Scottsdale Funds","VTWG",
	  "Vanguard Dividend Appreciation ETF","VIG",
	  "Claymore/Zacks Mid-Cap Core (ETF)","CZA",
	  "SPDR S&P MidCap 400 ETF","MDY",
	  "iShares S&P 1500 Index Fund (ETF)","ITOT",
	  "iShares Russell Midcap Growth Idx. (ETF)","IWP",
	  "iShares Russell 1000 Value Index (ETF)","IWD",
	  "Vanguard Health Care ETF","VHT",
	  "iShares Dow Jones US Technology (ETF)","IYW",
	  "Schwab Strategic Trust","SCHM",
	  "Claymore/Zacks Multi-Asset Inc Idx (ETF)","CVY",
	  "Vanguard Energy ETF","VDE",
	  "Vanguard Mid-Cap Growth ETF","VOT",
	  "iShares Russell 3000 Index (ETF)","IWV",
	  "iShares S&P MidCap 400 Growth (ETF)","IJK",
	  "Schwab U S Broad Market ETF","SCHB",
	  "Market Vectors Oil Services ETF","OIH",
	  "iShares S&P 500 Value Index (ETF)","IVE",
	  "PowerShares Exchange-Traded Fund Trust II","KBWD",
	  "Vanguard Value ETF","VTV",
	  "Technology SPDR (ETF)","XLK",
	  "iShares S&P NA Tech. Sec. Idx. Fd. (ETF)","IGM",
	  "iShares Morningstar Mid Growth Idx (ETF)","JKH",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY INDUSTRIALS S&P US SEL SECTOR SOURCE ETF","XLIS",
	  "Schwab U.S. Large-Cap ETF","SCHX",
	  "PowerShare Buyback Achievers Fund (ETF)","PKW",
	  "WisdomTree Equity Income Fund (ETF)","DHS",
	  "iShares S&P NA Tec.-Mul. Net. Idx. (ETF)","IGN",
	  "First Trust Mid Cap Core Alpha Fnd (ETF)","FNX",
	  "Energy Select Sector SPDR (ETF)","XLE",
	  "Vanguard High Dividend Yield ETF","VYM",
	  "PowerShares Dynamic Basic Material (ETF)","PYZ",
	  "PowerShares Fundamentl Pr Md Grwth Prtfl","PXMG",
	  "iShares Dow Jones US Oil Equip. (ETF)","IEZ",
	  "PowerShares Dynamic Lg.Cap Growth (ETF)","PWB",
	  "Consumer Staples Select Sect. SPDR (ETF)","XLP",
	  "First Trust Financials AlphaDEX Fd(ETF)","FXO",
	  "iShares Dow Jones US Consumer Ser. (ETF)","IYC",
	  "PowerShares Dyn Leisure & Entert. (ETF)","PEJ",
	  "PowerShares High Yld. Dividend Achv(ETF)","PEY",
	  "Deutsche Bank AG ELEMENTS Dogs of the Dow Total Return Index Note ELEMENTS Dogs of the Dow Linked to the Dow Jones High Yield Select 10 Total Return Index due N","DOD",
	  "Vanguard Industrials ETF","VIS",
	  "SPDR S&P Semiconductor (ETF)","XSD",
	  "Powershares Water Resource Portfolio","PHO",
	  "iShares S&P NA Tec.-SW. Idx. Fund (ETF)","IGV",
	  "Direxion Daily Semiconductor Bull 3X Shs","SOXL",
	  "iShares Cohen & Steers Realty Maj. (ETF)","ICF",
	  "Vanguard Consumer Staples ETF","VDC",
	  "First Trust Value Line Dividend Indx Fnd","FVD",
	  "Vanguard Small-Cap Value ETF","VBR",
	  "Vanguard Scottsdale Funds","VONV",
	  "Vanguard Materials ETF","VAW",
	  "PowerShares Dynamic Semiconductors (ETF)","PSI",
	  "Vanguard Scottsdale Funds","VONG",
	  "SPDR S&P Oil & Gas Equipt & Servs. (ETF)","XES",
	  "iShares Dow Jones US Oil & Gas Exp.(ETF)","IEO",
	  "First Trust Small Cap Cr AlphaDEXFd(ETF)","FYX",
	  "iShares Dow Jones Transport. Avg. (ETF)","IYT",
	  "iShares S&P NA Nat. Re. Sc. Idx. Fd(ETF)","IGE",
	  "RevenueShares Financials Sector (ETF)","RWW",
	  "First Trust Health Care AlphaDEX Fd(ETF)","FXH",
	  "SPDR S&P 500 Growth ETF","SPYG",
	  "ProShares Credit Suisse 130/30 (ETF)","CSM",
	  "First Trust Morningstar Divid Ledr (ETF)","FDL",
	  "ProShares UltraShort Semiconductors(ETF)","SSG",
	  "PowerShares Dynamic Software (ETF)","PSJ",
	  "ProShares Ultra Basic Materials (ETF)","UYM",
	  "PowerShares Dynamic Oil & Gas Serv (ETF)","PXJ",
	  "Schwab U S Large Cap Growth ETF","SCHG",
	  "Baronsmead VCT 2 PLC","BVT",
	  "Materials Select Sector SPDR","XLB",
	  "PowerShares Dynamic Food & Beverage(ETF)","PBJ",
	  "ProShares UltraShrt Rusell2000 Grwt(ETF)","SKK",
	  "Vanguard Scottsdale Funds","VTWV",
	  "First Trust Dow Jones Sel.MicroCap (ETF)","FDM",
	  "Vanguard Admiral Funds","VIOV",
	  "PowerShares Glbl Clean Enrgy Port (ETF)","PBD",
	  "First Trust Mult Cap Val Alpha Fnd (ETF)","FAB",
	  "iShares Russell Top 200 Grow Index Fund","IWY",
	  "ProShares UltraShort Basic Materls (ETF)","SMN",
	  "SPDR Dow Jones Large Cap ETF","ELR",
	  "iShares Morningstar Large Core Idx (ETF)","JKD",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY ENERGY S&P US SEL SECTOR SOURCE ETF","XLES",
	  "First Trust NYSE Arca Biotchnlgy Indx Fd","FBT",
	  "Guggenheim S&P 500 Pure Growth ETF","RPG",
	  "Vanguard Mid-Cap ETF","VO",
	  "iShares MSCI KLD 400 Social Idx Fd (ETF)","DSI",
	  "iShares Morningstar Small Core Idx (ETF)","JKJ",
	  "Vanguard Scottsdale Funds","VONE",
	  "WisdomTree Total Dividend Fund (ETF)","DTD",
	  "Rydex S&P 500 Pure Value ETF","RPV",
	  "First Trust Tech AlphaDEX Fnd (ETF)","FXL",
	  "Claymore/Sabrient Defensive Eq Idx (ETF)","DEF",
	  "First Trust Cnsumer Stapl Alpha Fd (ETF)","FXG",
	  "First Trust IPOX-100 Index Fund (ETF)","FPX",
	  "iShares Russell Top 200 Index Fund ETF","IWL",
	  "iShares S&P Aggressive Allocation Fd ETF","AOA",
	  "Vanguard Admiral Funds","VIOO",
	  "Claymore/Raymond James SB-1 Equity Fund","RYJ",
	  "iPath Long Extended Russell 1000 TR Index ETN","ROLA",
	  "Direxion Shares Exchange Traded Fund Trust","RETS",
	  "Powershares Active AlphaQ Fund (ETF)","PQY",
	  "iShares S&P Target Date 2015 Indx Fd ETF","TZE",
	  "Powershares Active Alpha M.C. Fund (ETF)","PQZ",
	  "Rydex 2x S&P 500 (ETF)","RSU",
	  "JETS Contrarian Opp Index Fund","JCO",
	  "SPDR Dow Jones Total Market (ETF)","TMW",
	  "iShares S&P Growth Allocation Fund (ETF)","AOR",
	  "Broadband HOLDRS","BDH",
	  "ProShares Ultra Russell1000 Growth (ETF)","UKF",
	  "PowerShares FTSE RAFI US 1000 (ETF)","PRF",
	  "iPath Long Extended Russell 2000 TR Index ETN","RTLA",
	  "SPDR Series Trust","XHE",
	  "iPath Short Extended Russell 1000 TR Index ETN","ROSA",
	  "Rydex S&P Equal Weight Materials(ETF)","RTM",
	  "WisdomTree MidCap Earnings Fund (ETF)","EZM",
	  "ProShares Ultra Russell MidCp Grth (ETF)","UKW",
	  "iShares S&P Target Date 2025 Indx Fd ETF","TZI",
	  "Internet Architecture HOLDRs (ETF)","IAH",
	  "WisdomTree LargeCap Growth Fund (ETF)","ROI",
	  "iShares S&P Target Date 2040 Indx Fd ETF","TZV",
	  "iShares MSCI USA Index Fund","EUSA",
	  "Rydex S&P Equal Weight Consumer Stap ETF","RHS",
	  "Barclays ETN+ Short D linked to the S&P 500 Total Return Index","BXDD",
	  "iShares S&P Target Date 2010 Indx Fd ETF","TZD",
	  "ProShares Ultra Telecommunications (ETF)","LTL",
	  "PowerShares Cleantech Portfolio (ETF)","PZD",
	  "ProShares Ultra SmallCap600 (ETF)","SAA",
	  "Wireless HOLDRs (ETF)","WMH",
	  "Credit Suisse AG ETN due October 6, 2020 linked to Credit Suisse Merger Arbitrage Liquid Index","CSMA",
	  "First Trust Ind/Prod AlphaDEX Fd (ETF)","FXR",
	  "UBS E-TRACS S&P 500 Gold Hedged ETN","SPGH",
	  "ProShares UltraShort Russell MValue(ETF)","SJL",
	  "Barclays Short B Leveraged S&P 500 TR ETN","BXDB",
	  "WisdomTree LargeCap Value Fund(ETF)","EZY",
	  "PowerShares WilderHill Prog. Ptf. (ETF)","PUW",
	  "PowerShares Dynamic MagniQuant Por.(ETF)","PIQ",
	  "Barclays Long B Leveraged S&P 500 TR ETN","BXUB",
	  "iPath Short Extended S&P 500 TR Index ETN","SFSA",
	  "iShares Global Completion Porfolio Builder Fund","XGC",
	  "ProShares Ultra Russell3000","UWC",
	  "PowerShare Dynamic Mid Cap Port. (ETF)","PXMC",
	  "Rydex S&P Equal Weight Industrials (ETF)","RGI",
	  "Wilshire 5000 Total Market ETF","WFVK",
	  "ProShares Ultra Russell2000 Value (ETF)","UVT",
	  "iShares NYSE 100 Index (ETF)","NY",
	  "ProShares UltraShort Industrials (ETF)","SIJ",
	  "Claymore/Ocean Tomo Growth Index ETF","OTR",
	  "PowerShares Exchange-Traded Fund Trust II","KBWP",
	  "First Trust Value Line Eq All Fnd (ETF)","FVI",
	  "PowerShares Dynamic Insurance Port.(ETF)","PIC",
	  "Rydex Inverse 2x S&P 500 (ETF)","RSW",
	  "Wilshire 4500 Completion ETF","WXSP",
	  "ProShares Short SmallCap600 (ETF)","SBB",
	  "FaithShares Methodist Values Fund ETF","FMV",
	  "iShares Dow Jones US Basic Mater. (ETF)","IYM",
	  "SPDR KBW Mortgage Finance ETF","KME",
	  "Claymore/Ocean Tomo Patent ETF","OTP",
	  "PowerShares Dynamic Mid Cap Value (ETF)","PXMV",
	  "SPDR Series Trust","XTN",
	  "PowerShare Dynamic Small Cap Port. (ETF)","PXSC",
	  "Internet Infrastructure HOLDRs (ETF)","IIH",
	  "RevenueShares Navellier Overal A-100 ETF","RWV",
	  "PowerShares Dynamic Banking Sec (ETF)","PJB",
	  "Market 2000+ HOLDRs (ETF)","MKH",
	  "ProShares Ultra Consumer Services (ETF)","UCC",
	  "PwrShs Mrngstr StkInvstr Cr Prtfl(ETF)","PYH",
	  "iShares S&P Target Date 2030 Indx Fd ETF","TZL",
	  "PowerShares Dynamic Market (ETF)","PWC",
	  "Market Vectors Environmental Service ETF","EVX",
	  "Powershares Active MegaCap Fund (ETF)","PMA",
	  "ProShares Shrt Basic Matrls ProShares","SBM",
	  "ProShares Short KBW Regional Banking ETF","KRS",
	  "JPMorgan Chase & Co KEYnotes ETN Linked to the First Trust 130/30 Large Cap Index","JFT",
	  "ProShares UltraShort Telecom (ETF)","TLL",
	  "ProShares Ultra Consumer Goods (ETF)","UGE",
	  "iPath Short Extended Russell 2000 TR Index ETN","RTSA",
	  "FaithShares Baptist Values Fund","FZB",
	  "DEUTSCHE BANK AKTIENGESELLSCHAFT ELEMENTS Linked to the Morningstar Wide Moat Focus Total Return Index due October 24, 2022","WMW",
	  "Direxion Shares Exchange Traded Fund Trust","RETL",
	  "iShares Morningstar Small Growth (ETF)","JKK",
	  "ProShares Ultra KBW Regional Banking ETF","KRU",
	  "ProShares Trust","RALS",
	  "FaithShares Christian Values Fund ETF","FOC",
	  "ProShares Ultra Utilities (ETF)","UPW",
	  "FaithShares Lutheran Values Fund","FKL",
	  "iPath Long Extended S&P 500 TR Index ETN","SFLA",
	  "iShares S&P Target Date 2020 Indx Fd ETF","TZG",
	  "iShares S&P Target Date Retirmt Incm ETF","TGR",
	  "B2B Internet HOLDRs (ETF)","BHH",
	  "ProShares Ultra Russell1000 Value (ETF)","UVG",
	  "Aktiebolaget Svensk Exportkredit (Swed Ex Cred Corp) Elements (SM) Linked to the SPECTRUM Large Cap U.S. Sector Momentum Index developed by BNP Paribas due Augu","EEH",
	  "Columbia Concentrated Large Cap Value","GVT",
	  "First Trust Strateg Val Idx Fnd (ETF)","FDV",
	  "Barclays Long C Leveraged S&P 500 TR ETN","BXUC",
	  "ProShares UltraShort Consumer Goods(ETF)","SZK",
	  "ProShares UltraShort Rusell1000 Vlu(ETF)","SJF",
	  "PowerShares Dynamic Consumer Sta. (ETF)","PSL",
	  "Barclays Short C Leveraged Inverse S&P 500 TR ETN","BXDC",
	  "Rydex ETF Trust","EWRM",
	  "Vanguard Telecommunication Services ETF","VOX",
	  "iShares Morningstar Mid Value Idx (ETF)","JKI",
	  "Wilshire US REIT ETF","WREI",
	  "First Trust Large Cap Core Alp Fnd (ETF)","FEX",
	  "SPDR S&P 400 Mid Cap Value ETF","MDYV",
	  "First Trust NASDAQ-100- Technology Ix Fd","QTEC",
	  "iShares Russell 3000 Growth Index (ETF)","IWZ",
	  "SPDR S&P Insurance ETF","KIE",
	  "ProShares UltraPro Short MidCap400 ETF","SMDD",
	  "Schwab U S Large Cap Value ETF","SCHV",
	  "PowerShares S&P 500 Hgh Qlty Prtfl (ETF)","SPHQ",
	  "Vanguard Admiral Funds","VOOG",
	  "Vanguard FTSE All-World ex-US ETF","VEU",
	  "PowerShares Dynamic Pharmaceuticals(ETF)","PJP",
	  "ProShares Ultra Semiconductors (ETF)","USD",
	  "Direxion Daily Energy Bear 3X Shs(ETF)","ERY",
	  "WisdomTree SmallCap Dividend Fund (ETF)","DES",
	  "Rydex S&P SmallCap 600 Pure Growth ETF","RZG",
	  "SPDR S&P Pharmaceuticals (ETF)","XPH",
	  "iShares MSCI USA ESG Select Scl Indx Fnd","KLD",
	  "Vanguard Admiral Funds","IVOV",
	  "Vanguard Admiral Funds","IVOG",
	  "iShares Dow Jones US Consumer Goods(ETF)","IYK",
	  "SPDR S&P Metals and Mining (ETF)","XME",
	  "PowerShares Zacks Micro Cap (ETF)","PZI",
	  "First Trust Energy AlphaDEX Fd (ETF)","FXN",
	  "PowerShares S&P 500 BuyWrite Portfol ETF","PBP",
	  "iShares S&P NA Tec. Semi. Idx. Fd.(ETF)","SOXX",
	  "First Trust Large Cap Value Opp Fnd(ETF)","FTA",
	  "ProShares UltraShort Rusell Mdcp G (ETF)","SDK",
	  "WisdomTree Total Earnings Fund (ETF)","EXT",
	  "iShares Morningstar Large Value (ETF)","JKF",
	  "WisdomTree LargeCap Dividend Fund (ETF)","DLN",
	  "ProShares UltraShort Rusell2000 Vlu(ETF)","SJH",
	  "iShares Russell Top 200 Value Index Fund","IWX",
	  "PowerShares Dynamic Indls Sec Port (ETF)","PRN",
	  "Rydex S&P MidCap 400 Pure Value ETF","RFV",
	  "First Trust Mult Cap Grwth Alp Fnd (ETF)","FAD",
	  "iShares Dow Jones US Medical Dev.(ETF)","IHI",
	  "RevenueShares Large Cap Fund (ETF)","RWL",
	  "SPDR Dow Jones Mid Cap ETF","EMM",
	  "PowerShares Dynamic Small Cap Value(ETF)","PXSV",
	  "iShares Dow Jones US Health Care(ETF)","IHF",
	  "iShares Dow Jones U.S. Index Fund (ETF)","IYY",
	  "Rydex S&P Equal Weight Consumer Dis ETF","RCD",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY UTILITIES S&P US SEL SECTOR SOURCE ETF","XLUS",
	  "iShares Morningstar Mid Core Index (ETF)","JKG",
	  "RevenueShares Mid Cap Fund (ETF)","RWK",
	  "First Trust Value Line 100 Fund","FVL",
	  "WisdomTree Dividend ex-Fin Fund(ETF)","DTN",
	  "ProShares Short Dow30 (ETF)","DOG",
	  "PowerShares Aerospace & Defense (ETF)","PPA",
	  "iPath Goldman Sachs Barclays Bank PLC iPath Exchange Traded Notes Linked to the CBOE S&P 500 BuyWrite Index Structured Product","BWV",
	  "iShares S&P Conservative Allocatn Fd ETF","AOK",
	  "Claymore/Sabrient Insider ETF","NFO",
	  "iShares Dow Jones US Home Const. (ETF)","ITB",
	  "PowerShares Dynamic Bldg. & Const. (ETF)","PKB",
	  "First Trust Materials AlphaDEX Fnd (ETF)","FXZ",
	  "iShares Morningstar Large Growth (ETF)","JKE",
	  "Vanguard Admiral Funds","VOOV",
	  "ProShares Ultra Russell MidCap Vlue(ETF)","UVU",
	  "PowerShares Listed Private Eq. (ETF)","PSP",
	  "PowerShares Dynamic Retail (ETF)","PMR",
	  "iShares S&P Moderate Allocation Fund ETF","AOM",
	  "PowerShares Dynamic Tech Sec (ETF)","PTF",
	  "ProShares Short MidCap400 (ETF)","MYY",
	  "PowerShares Dynamic Utilities (ETF)","PUI",
	  "ProShares Short S&P500 (ETF)","SH",
	  "iShares NYSE Composite Index (ETF)","NYC",
	  "First Trust Large Cap GO Alpha Fnd (ETF)","FTC",
	  "SPDR MSCI ACWI ex-US (ETF)","CWI",
	  "Vanguard Scottsdale Funds","VTHR",
	  "ProShares Short Real Estate ProShares","REK",
	  "First Trust NASDAQ Clean Edge Green(ETF)","QCLN",
	  "ProShares Short Financials (ETF)","SEF",
	  "First Trust NASDAQ-100 EqualWeighted ETF","QQEW",
	  "RevenueShares Small Cap Fund (ETF)","RWJ",
	  "SPDR S&P 600 Small Cap Growth ETF","SLYG",
	  "Vanguard Admiral Funds","IVOO",
	  "ProShares UltraShort Health Care (ETF)","RXD",
	  "ProShares Short Russell2000(ETF)","RWM",
	  "ProShares Short QQQ (ETF)","PSQ",
	  "ProShares Short Oil & Gas (ETF)","DDG",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY CONS STAPLE S&P US SEL SECTOR SOURCE ETF","XLPS",
	  "Rydex S&P Equal Weight Utilities (ETF)","RYU",
	  "SPDR S&P 500 Value ETF","SPYV",
	  "ProShares UltraShort Utilities (ETF)","SDP",
	  "iShares S&P Target Date 2035 Indx Fd ETF","TZO",
	  "ProShares UltraShort Nasdaq Biotech","BIS",
	  "PowerShares Dynamic Finl Sec Fnd (ETF)","PFI",
	  "SPDR Morgan Stanley Technology (ETF)","MTK",
	  "SPDR S&P Homebuilders (ETF)","XHB",
	  "PowerShares Dynamic Heathcare Sec (ETF)","PTH",
	  "Rydex ETF Trust","EWRI",
	  "iShares Dow Jones US Pharm Indx (ETF)","IHE",
	  "SPDR S&P 400 Mid Cap Growth ETF","MDYG",
	  "Rydex ETF Trust Rydex S&P Equal Weight F","RYF",
	  "ProShares UltraShort Dow30 (ETF)","DXD",
	  "First Trust NASDAQ ABA Comm Bnk Indx Fnd","QABA",
	  "iShares Dow Jones US Insurance Index ETF","IAK",
	  "ProShares Ultra Industrials (ETF)","UXI",
	  "PowerShares Dynamic Energy Sector (ETF)","PXI",
	  "ProShares UltraShort Financials (ETF)","SKF",
	  "Vanguard Admiral Funds","VIOG",
	  "ProShares UltraShort S&P500 (ETF)","SDS",
	  "ProShares UltraShort Real Estate (ETF)","SRS",
	  "ProShares UltraShort Russell2000 (ETF)","TWM",
	  "PowerShares Lux Nanotech (ETF)","PXN",
	  "Rydex S&P Equal Weight Energy (ETF)","RYE",
	  "ProShares UltraPro Short Dow30 ETF","SDOW",
	  "ProShares UltraShort Oil & Gas (ETF)","DUG",
	  "Direxion Daily Large Cap Bear 3X Shares","SPXS",
	  "ProShares UltraPro Short S&P 500 (ETF)","SPXU",
	  "ProShares UltraShort Technology (ETF)","REW",
	  "ProShares UltraPro Short Russell2000 ETF","SRTY",
	  "ProShares UltraShort Russell1000 (ETF)","SFK",
	  "PowerShares Dynamic Consumer Disc. (ETF)","PEZ",
	  "ProShares UltraShort QQQ (ETF)","QID",
	  "iPath S&P 500 VIX Mid-Term Futures ETN","VXZ",
	  "PowerShares Dynamic Energy Explor. (ETF)","PXE",
	  "Direxion Daily Small Cap Bear 3X Shares","TZA",
	  "Direxion Daily Financial Bear 3X Shares","FAZ",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY HEALTH CARE S&P US SEL SECTOR SOURCE ETF","XLVS",
	  "ProShares UltraShort Consumer Serv (ETF)","SCC",
	  "The Prospect Japan Fund Limited","PJF",
	  "ProShares UltraShort Russell3000","TWQ",
	  "ProShares Trust UltraPro Short QQQ ETF","SQQQ",
	  "SOURCE MARKETS PUBLIC LIMITED COMPANY MATERIALS S&P US SEL SECTOR SOURCE ETF","XLBS",
	  "Market Vectors Gold Miners ETF","GDX",
	  "iPath S&P 500 VIX Short Term Futures TM ETN","VXX",
	  "Utilities HOLDRs (ETF)","UTHYL",
	  "Internet HOLDRs (ETF)","HHHYL",
	  "Claymore/Zacks Sector Rotation ETF","XRO",
};
  
  @SuppressWarnings("unchecked")
  public static void main(String[] args){
    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
        
    //HashMap<String,String> etfSet = new HashMap<String,String>();

    Map<String, StockGroup> groupMap = new HashMap<String, StockGroup>();
    for(StockGroup g : (List<StockGroup>) pm.newQuery(StockGroup.class).execute()){
        groupMap.put(g.getCode(), g);
      }
    
    Exchange nasdaq =  ((List<Exchange>) pm.newQuery(Exchange.class,"this.code == 'NASDAQ'").execute()).get(0);
    
     Set<String> existingStocks = new HashSet<String>();
    for(Stock s : (List<Stock>) pm.newQuery(Stock.class).execute()){
    	existingStocks.add(s.getCode());
    }        
        //"this.description.toUpperCase().matches(\".*ETF.*\")");
    
    
    for(int i=0; i< etfs.length; i+=2){
    	String code =etfs[i+1];
    	String desc = etfs[i]; 

    	if(!existingStocks.contains(code)){    
    		Stock ns = new Stock(code,desc,nasdaq);
    		pm.makePersistent(ns);
    	      String groupName = "NASDAQ_ETF";
    	      StockGroup g = groupMap.get(groupName);
    	      g.addStock(ns);
    	}
    	
      //System.out.println(s.getCode() + " " + s.getDescription());
      //String exchange = s.getExchange().getCode();
      //      
      //if(g == null){
        //g = new StockGroup(groupName, groupName);
        //groupMap.put(groupName,g);
      //}
      //else{
    	//  if(g.getStocks().contains(s)) continue;
      //}
      

    }
    
    for (StockGroup g : groupMap.values()) {
      pm.makePersistent(g);
    }

  }
}
