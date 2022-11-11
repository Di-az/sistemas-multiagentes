from mesa import Agent, Model
from mesa.space import Grid
from mesa.time import RandomActivation

from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

from mesa.visualization.UserParam import UserSettableParameter

from mesa.datacollection import DataCollector
from mesa.visualization.modules import ChartModule


class Tree(Agent):
    FINE = 0
    BURNING = 1
    BURNED_OUT = 2
    def __init__(self, model: Model):
        super().__init__(model.next_id(), model)
        self.condition = self.FINE

    def step(self):
        if self.condition == self.BURNING:
            for neighbor in self.model.grid.neighbor_iter(self.pos, moore=False):
                if neighbor.condition == self.FINE:
                    neighbor.condition = self.BURNING
            self.condition = self.BURNED_OUT

class Forest(Model):
    def __init__(self, height=50, width=50, density=0.45):
        super().__init__()
        self.schedule = RandomActivation(self)
        self.grid = Grid(height, width, torus=False)
        for _,x,y in self.grid.coord_iter():
            if self.random.random() < density:
                tree = Tree(self)
                if x == 0:
                    tree.condition = Tree.BURNING
                self.grid.place_agent(tree, (x,y))
                self.schedule.add(tree)
        
        self.datacollector = DataCollector({"Percent burned": lambda m: self.count_type(m, Tree.BURNED_OUT) / len(self.schedule.agents)})

    @staticmethod
    def count_type(model, condition):
        count = 0
        for tree in model.schedule.agents:
            if tree.condition == condition:
                count += 1
        return count

    def step(self):
        self.schedule.step()
        self.datacollector.collect(self)

        if self.count_type(self, Tree.BURNING) == 0:
            self.running = False

def agent_portrayal(agent):
    if agent.condition == Tree.FINE:
        portrayal = {"Shape": "circle", "Filled": "true", "Color": "Green", "r": 0.75, "Layer": 0}
    elif agent.condition == Tree.BURNING:
        portrayal = {"Shape": "circle", "Filled": "true", "Color": "Red", "r": 0.75, "Layer": 0}
    elif agent.condition == Tree.BURNED_OUT:
        portrayal = {"Shape": "circle", "Filled": "true", "Color": "Gray", "r": 0.75, "Layer": 0}
    else:
        portrayal = {}

    return portrayal

grid = CanvasGrid(agent_portrayal, 50, 50, 450, 450)
# server = ModularServer(Forest, [grid], "Forest", {})

# server = ModularServer(Forest, [grid], "Forest", {
#     "density": UserSettableParameter("slider","Tree density", 0.45, 0.01, 1.0, 0.01),
#     "width":50, "height":50
# })

chart = ChartModule([{"Label": "Percent burned", "Color": "Black"}], data_collector_name='datacollector')

server = ModularServer(Forest,
                       [grid, chart],
                       "Forest",
                       {"density": UserSettableParameter("slider", "Tree density", 0.45, 0.01, 1.0, 0.01), "width":50, "height":50})

server.port = 8522 # The default
server.launch()