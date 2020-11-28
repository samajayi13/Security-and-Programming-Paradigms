

# INSERT INTO userAccounts VALUES
# ('Sara','man','sara.fletcher@myemail.co.uk','44-0191-5678901','woman123','m01anjdsjd1nf9232vdsidikfo2sh11dshdshsdhdshsdhdshsdhdshdshhsdf','User');


drop table RandomLotteryDraw;
CREATE TABLE RandomLotteryDraw
(
    ID int auto_increment,
    Numbers varchar(50) not null,
    TimeCreated date not null,
    primary key(id)
);
insert into RandomLotteryDraw(Numbers,TimeCreated)
values("15,52,22,46,37,46",now());


select * from userAccounts;





